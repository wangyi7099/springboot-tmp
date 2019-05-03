package cn.stylefeng.guns.config.flowable;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.flowable.common.engine.impl.cfg.IdGenerator;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.FlowableMailProperties;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.app.FlowableAppProperties;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.flowable.spring.boot.process.Process;
import org.flowable.spring.boot.process.ProcessAsync;
import org.flowable.spring.boot.process.ProcessAsyncHistory;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(FlowableProperties.class)
public class FlowDataSourceProcessEngineAutoConfiguration extends ProcessEngineAutoConfiguration {

    @Value("${flowable.diagram.activityFontName}")
    private String activityFontName;

    @Value("${flowable.diagram.labelFontName}")
    private String labelFontName;

    @Value("${flowable.diagram.annotationFontName}")
    private String annotationFontName;


    public FlowDataSourceProcessEngineAutoConfiguration(FlowableProperties flowableProperties,
                                                        FlowableProcessProperties processProperties, FlowableAppProperties appProperties, FlowableIdmProperties idmProperties,
                                                        FlowableMailProperties mailProperties) {
        super(flowableProperties, processProperties, appProperties, idmProperties, mailProperties);
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager,
                                                                             @Process ObjectProvider<IdGenerator> processIdGenerator,
                                                                             ObjectProvider<IdGenerator> globalIdGenerator,
                                                                             @ProcessAsync ObjectProvider<AsyncExecutor> asyncExecutorProvider,
                                                                             @ProcessAsyncHistory ObjectProvider<AsyncExecutor> asyncHistoryExecutorProvider) throws IOException {

        SpringProcessEngineConfiguration conf = super.springProcessEngineConfiguration(dataSource, platformTransactionManager, processIdGenerator, globalIdGenerator, asyncExecutorProvider, asyncHistoryExecutorProvider);
        //conf.setIdmEngineConfigurator(idmEngineConfigurator(dataSource));
        conf.setActivityFontName(activityFontName);
        conf.setLabelFontName(labelFontName);
        conf.setAnnotationFontName(annotationFontName);

        return conf;
    }

    /**
     * 数据库更新脚本 不添加的话
     * 工作流就没有以下三张表
     * act_de_model
     * act_de_model_history
     * act_de_model_relation
     *
     * @author yaoliguo
     * @date 2019-05-03 11:26
     */
    @Bean
    public Liquibase liquibase(DataSource dataSource) {

        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            database.setDatabaseChangeLogTableName(database.getDatabaseChangeLogTableName());
            database.setDatabaseChangeLogLockTableName(database.getDatabaseChangeLogLockTableName());

            Liquibase liquibase = new Liquibase("META-INF/liquibase/flowable-modeler-app-db-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("flowable");
            return liquibase;

        } catch (Exception e) {
            throw new RuntimeException("Error creating liquibase database", e);
        }
    }

    /**
     * 创建这个配置类 启动工作流modeler配置
     *
     * @author yaoliguo
     * @date 2019-05-03 11:27
     */
    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        FlowableModelerAppProperties flowableModelerAppProperties = new FlowableModelerAppProperties();
        return flowableModelerAppProperties;
    }

}
