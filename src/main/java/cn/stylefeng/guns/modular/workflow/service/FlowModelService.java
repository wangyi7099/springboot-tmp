package cn.stylefeng.guns.modular.workflow.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import cn.stylefeng.guns.modular.workflow.mapper.FlowModelMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlowModelService extends ServiceImpl<FlowModelMapper, FlowModel> {


    public Page<FlowModel> getModelList(String name, String key) {

        Page page = LayuiPageFactory.defaultPage();
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("key", key);

        return baseMapper.getModelList(page, param);
    }


}
