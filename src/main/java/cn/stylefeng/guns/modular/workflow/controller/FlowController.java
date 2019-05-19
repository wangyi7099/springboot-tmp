package cn.stylefeng.guns.modular.workflow.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.modular.workflow.entity.FlowModel;
import cn.stylefeng.guns.modular.workflow.service.FlowModelService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Object deploy(@RequestParam(required = false) String key) {

        processEngine.getRepositoryService().createDeployment().key(key).deploy();


        return ResultData.success();
    }

}
