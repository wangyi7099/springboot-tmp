package cn.stylefeng.guns.modular.workflow.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import cn.stylefeng.guns.modular.workflow.service.FlowModelService;
import cn.stylefeng.guns.modular.workflow.service.FlowService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gunsApi/flow")
public class FlowController extends BaseController {

    private static String PREFIX = "/modular/flow/model/";

    @Autowired
    FlowModelService flowModelService;

    @Autowired
    FlowService flowService;

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
    public Object deploy(@RequestParam(required = false) String flowKey) {

        String deploy = flowModelService.deploy(flowKey);
        if (deploy != null) {
            return ResultData.success(deploy);
        }

        return ResultData.error("部署失败");
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

        return ResultData.success(flowService.findTask(assignee));
    }

    /**
     * 查询已办任务
     *
     * @author yaoliguo
     * @date 2019-05-19 18:33
     */
    @ApiOperation(value = "查询已办任务", notes = "查询已办任务")
    @RequestMapping(value = "/getHisTaskPage", method = RequestMethod.GET)
    @ResponseBody
    public Object getHisTaskPage(@RequestParam(required = false) String assignee) {

        return ResultData.success(flowService.getHisTaskPage(assignee));
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

    @ApiOperation(value = "查看运行的流程图", notes = "查看运行的流程图")
    @RequestMapping(value = "/getRunPic", method = RequestMethod.GET)
    public void getRunPic(@RequestParam(required = false) String procInstId) {

        ServletOutputStream outputStream = null;
        InputStream in = null;

        try {
            ProcessInstance pi = processEngine.getRuntimeService()//
                    .createProcessInstanceQuery()//
                    .processInstanceId(procInstId)
                    .singleResult();
            // 流程走完的不显示图
            if (pi == null) {
                return;
            }

            List<Execution> executions = processEngine//
                    .getRuntimeService()//
                    .createExecutionQuery()//
                    .processInstanceId(procInstId)//
                    .list();

            // 得到正在执行的Activity的Id
            List<String> activityIds = new ArrayList<>();
            List<String> flows = new ArrayList<>();
            for (Execution exe : executions) {
                List<String> ids = processEngine.getRuntimeService().getActiveActivityIds(exe.getId());
                activityIds.addAll(ids);
            }

            // 获取流程图
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(pi.getProcessDefinitionId());

            ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
            ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
            in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
                    engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
                    engconf.getClassLoader(), 1.0, true);

            HttpServletResponse httpServletResponse = getHttpServletResponse();
            outputStream = httpServletResponse.getOutputStream();

            byte[] buf = new byte[1024];
            int l = 0;
            while ((l = in.read(buf, 0, 1024)) > 0) {
                outputStream.write(buf, 0, l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
