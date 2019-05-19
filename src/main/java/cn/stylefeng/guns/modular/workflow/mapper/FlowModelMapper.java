package cn.stylefeng.guns.modular.workflow.mapper;

import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface FlowModelMapper extends BaseMapper<FlowModel> {

    /**
     * 查询有哪些模型
     *
     * @author yaoliguo
     * @date 2019-05-19 15:37
     */
    Page<FlowModel> getModelList(@Param("page") Page page, @Param("map") Map<String, Object> map);

}
