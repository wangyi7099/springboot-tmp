package cn.stylefeng.guns.modular.frontapi.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.dictmap.DeptDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.node.AntTreeNode;
import cn.stylefeng.guns.core.common.node.TreeviewNode;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.modular.system.entity.Dept;
import cn.stylefeng.guns.modular.system.model.DeptDto;
import cn.stylefeng.guns.modular.system.service.DeptService;
import cn.stylefeng.guns.modular.system.warpper.DeptTreeWrapper;
import cn.stylefeng.guns.modular.system.warpper.DeptWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.treebuild.DefaultTreeBuildFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gunsApi/dept")
public class DeptApiController extends BaseController {

    @Autowired
    private DeptService deptService;


    /**
     * 获取部门的tree列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @ApiOperation(value = "获取部门树", notes = "获取部门树结构")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseData tree() {
        List<AntTreeNode> tree = this.deptService.antTree();
        tree.add(AntTreeNode.createParent());
        return ResultData.success(tree);
    }

    /**
     * 获取部门的tree列表，treeview格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/treeview", method = RequestMethod.GET)
    @ApiOperation(value = "获取部门的tree列表", notes = "获取部门的tree列表，treeview格式")
    public ResponseData treeview() {
        List<TreeviewNode> treeviewNodes = this.deptService.treeviewNodes();

        //构建树
        DefaultTreeBuildFactory<TreeviewNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("0");
        List<TreeviewNode> results = factory.doTreeBuild(treeviewNodes);

        //把子节点为空的设为null
        DeptTreeWrapper.clearNull(results);

        return ResultData.success(results);
    }

    /**
     * 新增部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加部门", notes = "添加部门")
    public ResponseData add(Dept dept) {
        this.deptService.addDept(dept);
        return ResultData.success();
    }

    /**
     * 获取所有部门列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有部门列表", notes = "获取所有部门列表")
    public ResponseData list(@RequestParam(value = "condition", required = false) String condition,
                             @RequestParam(value = "deptId", required = false) Long deptId) {
        Page<Map<String, Object>> list = this.deptService.list(condition, deptId);
        Page<Map<String, Object>> wrap = new DeptWrapper(list).wrap();
        return ResultData.success(wrap);
    }

    /**
     * 部门详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/{deptId}/detail", method = RequestMethod.GET)
    @ApiOperation(value = "部门详情", notes = "部门详情")
    public ResponseData detail(@PathVariable("deptId") Long deptId) {
        Dept dept = deptService.getById(deptId);
        DeptDto deptDto = new DeptDto();
        BeanUtil.copyProperties(dept, deptDto);
        deptDto.setPName(ConstantFactory.me().getDeptName(deptDto.getPid()));
        return ResultData.success(deptDto);
    }

    /**
     * 修改部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "修改部门", notes = "修改部门")
    public ResponseData update(Dept dept) {
        deptService.editDept(dept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除部门", notes = "删除部门")
    public ResponseData delete(@RequestParam Long deptId) {

        deptService.deleteDept(deptId);

        return SUCCESS_TIP;
    }

}
