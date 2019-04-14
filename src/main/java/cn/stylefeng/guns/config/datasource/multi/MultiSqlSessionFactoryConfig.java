/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.config.datasource.multi;

import cn.stylefeng.roses.core.config.properties.DruidProperties;
import cn.stylefeng.roses.core.mutidatasource.mybatis.OptionalSqlSessionTemplate;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多数据源配置<br/>
 * <p>
 * 注：由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
 *
 * @author stylefeng
 * @Date 2017/5/20 21:58
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "guns.muti-datasource", name = "open", havingValue = "true")
public class MultiSqlSessionFactoryConfig {

    private final MybatisPlusProperties properties;

    private final Interceptor[] interceptors;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;

    private final ApplicationContext applicationContext;


    public MultiSqlSessionFactoryConfig(MybatisPlusProperties properties,
                                        ObjectProvider<Interceptor[]> interceptorsProvider,
                                        ResourceLoader resourceLoader,
                                        ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                        ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
                                        ApplicationContext applicationContext) {
        this.properties = properties;
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        this.applicationContext = applicationContext;
    }

    /**
     * 主sqlSessionFactory
     */
    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactoryPrimary(@Qualifier("dataSourcePrimary") DataSource dataSource) {
        return createSqlSessionFactory(dataSource);
    }

    /**
     * 第二个sqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBusiness(@Qualifier("dataSourceBusiness") DataSource dataSource) {
        return createSqlSessionFactory(dataSource);
    }

    /**
     * 多数据源sqlSessionTemplate切换模板
     */
    @Bean(name = "gunsSqlSessionTemplate")
    public OptionalSqlSessionTemplate gunsSqlSessionTemplate(@Qualifier("sqlSessionFactoryPrimary") SqlSessionFactory sqlSessionFactoryPrimary,
                                                             @Qualifier("sqlSessionFactoryBusiness") SqlSessionFactory sqlSessionFactoryBusiness,
                                                             @Qualifier("druidProperties") DruidProperties druidProperties,
                                                             @Qualifier("mutiDataSourceProperties") DruidProperties mutiDataSourceProperties) {
        if (ToolUtil.isOneEmpty(druidProperties, druidProperties.getDataSourceName())) {
            throw new IllegalArgumentException("初始化OptionalSqlSessionTemplate错误！请设置spring.datasource.data-source-name属性的值！");
        }

        if (ToolUtil.isOneEmpty(mutiDataSourceProperties, mutiDataSourceProperties.getDataSourceName())) {
            throw new IllegalArgumentException("初始化OptionalSqlSessionTemplate错误！请设置spring.muti-datasource.data-source-name属性的值！");
        }

        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        sqlSessionFactoryMap.put(druidProperties.getDataSourceName(), sqlSessionFactoryPrimary);
        sqlSessionFactoryMap.put(mutiDataSourceProperties.getDataSourceName(), sqlSessionFactoryBusiness);
        return new OptionalSqlSessionTemplate(sqlSessionFactoryPrimary, sqlSessionFactoryMap);
    }

    /**
     * 创建数据源
     */
    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) {
        try {
            MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
            factory.setDataSource(dataSource);
            factory.setVfs(SpringBootVFS.class);
            if (!ObjectUtils.isEmpty(this.interceptors)) {
                factory.setPlugins(this.interceptors);
            }
            if (this.databaseIdProvider != null) {
                factory.setDatabaseIdProvider(this.databaseIdProvider);
            }
            if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
                factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
            }
            if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
                factory.setMapperLocations(this.properties.resolveMapperLocations());
            }
            GlobalConfig globalConfig = this.properties.getGlobalConfig();
            if (this.applicationContext.getBeanNamesForType(MetaObjectHandler.class,
                    false, false).length > 0) {
                MetaObjectHandler metaObjectHandler = this.applicationContext.getBean(MetaObjectHandler.class);
                globalConfig.setMetaObjectHandler(metaObjectHandler);
            }

            //globalConfig中有缓存sqlSessionFactory，目前还没别的办法
            SqlSessionFactory sqlSessionFactory = factory.getObject();
            globalConfig.signGlobalConfig(sqlSessionFactory);

            factory.setGlobalConfig(globalConfig);
            return factory.getObject();
        } catch (Exception e) {
            log.error("初始化SqlSessionFactory错误！", e);
            throw new ServiceException(500, "初始化SqlSessionFactory错误！");
        }
    }

}
