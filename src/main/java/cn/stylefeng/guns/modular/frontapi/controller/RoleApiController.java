package cn.stylefeng.guns.modular.frontapi.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.dictmap.RoleDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.system.entity.Role;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.model.RoleDto;
import cn.stylefeng.guns.modular.system.service.RoleService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.system.warpper.RoleWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/gunsApi/role")
public class RoleApiController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData list(@RequestParam(value = "roleName", required = false) String roleName) {
        Page<Map<String, Object>> roles = this.roleService.selectRoles(roleName);
        Page<Map<String, Object>> wrap = new RoleWrapper(roles).wrap();
        return ResponseData.success(wrap);
    }

    /**
     * 角色新增
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "角色新增", notes = "角色新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    @ResponseBody
    public ResponseData add(Role role) {
        this.roleService.addRole(role);
        return SUCCESS_TIP;
    }

    /**
     * 角色修改
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "角色修改", notes = "角色修改")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    @ResponseBody
    public ResponseData edit(RoleDto roleDto) {
        this.roleService.editRole(roleDto);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @BussinessLog(value = "删除角色", key = "roleId", dict = DeleteDict.class)
    @ResponseBody
    public ResponseData remove(@RequestParam Long roleId) {

        //缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(roleId));

        this.roleService.delRoleById(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 查看角色
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "查看角色", notes = "查看角色")
    @RequestMapping(value = "/{roleId}/view", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData view(@PathVariable Long roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = this.roleService.getById(roleId);
        Map<String, Object> roleMap = BeanUtil.beanToMap(role);

        Long pid = role.getPid();
        String pName = ConstantFactory.me().getSingleRoleName(pid);
        roleMap.put("pName", pName);

        return ResponseData.success(roleMap);
    }

    /**
     * 配置权限
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "配置权限", notes = "配置权限")
    @RequestMapping(value = "/setAuthority", method = RequestMethod.POST)
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    @ResponseBody
    public ResponseData setAuthority(@RequestParam("roleId") Long roleId, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "配置权限", notes = "配置权限")
    @RequestMapping(value = "/roleTreeList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleService.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return ResponseData.success(roleTreeList);
    }

    /**
     * 获取角色列表，通过用户id
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @ApiOperation(value = "配置权限", notes = "配置权限")
    @RequestMapping(value = "/{userId}/roleTreeListByUserId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData roleTreeListByUserId(@PathVariable Long userId) {
        User theUser = this.userService.getById(userId);
        String roleId = theUser.getRoleId();
        if (ToolUtil.isEmpty(roleId)) {
            return ResponseData.success(roleService.roleTreeList());
        } else {

            String[] strArray = roleId.split(",");

            //转化成Long[]
            Long[] longArray = new Long[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                longArray[i] = Long.valueOf(strArray[i]);
            }

            return ResponseData.success(roleService.roleTreeListByRoleId(longArray));
        }
    }
}
