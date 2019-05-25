package cn.stylefeng.guns.modular.workflow.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import cn.stylefeng.guns.modular.workflow.mapper.FlowModelMapper;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * model相关的服务类
 *
 * @author yaoliguo
 * @date 2019-05-25 09:44
 * ©2018 版权所有
 */
@Service
public class FlowModelService extends ServiceImpl<FlowModelMapper, FlowModel> {

    @Autowired
    private ProcessEngine processEngine;

    public Page<FlowModel> getModelList(String name, String key) {

        Page page = LayuiPageFactory.defaultPage();
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("key", key);

        return baseMapper.getModelList(page, param);
    }

    @Transactional(rollbackFor = Exception.class, timeout = 120)
    public String deploy(String flowKey) {

        Map<String, Object> modelFlie = baseMapper.selectFlowFileByKey(flowKey);
        String flowBlob = (String) modelFlie.get("model_editor_json");
        String name = (String) modelFlie.get("name");
        try {
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(flowBlob.getBytes());
            byte[] bpmnBytes = null;
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode, null, null);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String processName = name + ".bpmn20.xml";
            DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment().name(name);
            Deployment deployment = deploymentBuilder.addBytes(processName, bpmnBytes).deploy();
            if (deployment == null) {
                throw new ServiceException(2000, "部署失败");
            }
            return deployment.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }


}
