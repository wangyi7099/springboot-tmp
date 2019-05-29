package cn.stylefeng.guns.modular.business.mapper;

import cn.stylefeng.guns.modular.business.entity.LeaveBill;
import cn.stylefeng.guns.modular.business.model.params.LeaveBillParam;
import cn.stylefeng.guns.modular.business.model.result.LeaveBillResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yaoliguo
 * @since 2019-05-27
 */
public interface LeaveBillMapper extends BaseMapper<LeaveBill> {

    /**
     * 获取列表
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    List<LeaveBillResult> customList(@Param("paramCondition") LeaveBillParam paramCondition);

    /**
     * 获取map列表
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") LeaveBillParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    Page<LeaveBillResult> customPageList(@Param("page") Page page, @Param("paramCondition") LeaveBillParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") LeaveBillParam paramCondition);

}
