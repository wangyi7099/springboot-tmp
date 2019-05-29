package cn.stylefeng.guns.modular.business.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.business.entity.LeaveBill;
import cn.stylefeng.guns.modular.business.model.params.LeaveBillParam;
import cn.stylefeng.guns.modular.business.service.LeaveBillService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 控制器
 *
 * @author yaoliguo
 * @Date 2019-05-27 21:14:23
 */
@Controller
@RequestMapping("/leaveBill")
public class LeaveBillController extends BaseController {

    private String PREFIX = "/modular/leaveBill";

    @Autowired
    private LeaveBillService leaveBillService;

    /**
     * 跳转到主页面
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/leaveBill.html";
    }

    /**
     * 新增页面
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/leaveBill_add.html";
    }

    /**
     * 编辑页面
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/leaveBill_edit.html";
    }

    /**
     * 新增接口
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(LeaveBillParam leaveBillParam) {
        this.leaveBillService.add(leaveBillParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(LeaveBillParam leaveBillParam) {
        this.leaveBillService.update(leaveBillParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(LeaveBillParam leaveBillParam) {
        this.leaveBillService.delete(leaveBillParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(LeaveBillParam leaveBillParam) {
        LeaveBill detail = this.leaveBillService.getById(leaveBillParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(LeaveBillParam leaveBillParam) {
        return this.leaveBillService.findPageBySpec(leaveBillParam);
    }

}


