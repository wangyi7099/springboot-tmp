package cn.stylefeng.guns.modular.workflow.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import cn.stylefeng.guns.modular.workflow.service.FlowModelService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.task.api.Task;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/gunsApi/flow")
public class FlowController extends BaseController {

    private static String PREFIX = "/modular/flow/model/";

    @Autowired
    FlowModelService flowModelService;

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 跳转到查看管理员列表的页面
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "model.html";
    }

    /**
     * 流程model列表
     *
     * @author yaoliguo
     * @date 2019-05-13 20:36
     */
    @ApiOperation(value = "流程model列表", notes = "流程model列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String key) {

        Page<FlowModel> modelList = flowModelService.getModelList(name, key);

        return LayuiPageFactory.createPageInfo(modelList);
    }

    /**
     * 部署流程
     *
     * @author yaoliguo
     * @date 2019-05-19 18:33
     */
    @ApiOperation(value = "部署model", notes = "部署model")
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ResponseBody
    public Object deploy(@RequestParam(required = false) String modelId) {


        try {
            BpmnModel bpmnModel1 = processEngine.getRepositoryService().getBpmnModel(modelId);
            Model modelData = processEngine.getRepositoryService().getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()//
                    .readTree(processEngine.getRepositoryService()//
                            .getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);

            //验证bpmnModel 是否是正确的bpmn xml文件
            ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
            ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
            //验证失败信息的封装ValidationError
            List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
            if (validate.size() > 0) {
                return ResultData.error(validate.toString());
            }
            bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment dep = processEngine.getRepositoryService()//
                    .createDeployment()//
                    .name(modelData.getName())//
                    .addString(processName, new String(bpmnBytes, "UTF-8"))//
                    .deploy();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResultData.success();
    }

    /**
     * 启动流程
     *
     * @author yaoliguo
     * @date 2019-05-19 18:33
     */
    @ApiOperation(value = "启动流程", notes = "启动流程")
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public Object start(@RequestParam(required = false) String key) {

        processEngine.getRuntimeService().startProcessInstanceByKey(key);


        return ResultData.success();
    }

    /**
     * 查询任务
     *
     * @author yaoliguo
     * @date 2019-05-19 18:33
     */
    @ApiOperation(value = "查询任务", notes = "查询任务")
    @RequestMapping(value = "/findTask", method = RequestMethod.GET)
    @ResponseBody
    public Object findTask(@RequestParam(required = false) String assignee) {

        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().list();


        return ResultData.success(list);
    }

    /**
     * 办理任务
     *
     * @author yaoliguo
     * @date 2019-05-19 18:33
     */
    @ApiOperation(value = "办理任务", notes = "办理任务")
    @RequestMapping(value = "/completeTask", method = RequestMethod.GET)
    @ResponseBody
    public Object completeTask(@RequestParam(required = false) String taskId) {

        processEngine.getTaskService().complete(taskId);


        return ResultData.success();
    }

}
