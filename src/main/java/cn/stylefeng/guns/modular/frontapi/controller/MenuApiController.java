package cn.stylefeng.guns.modular.frontapi.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.dictmap.MenuDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.node.AntTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.core.util.JwtTokenUtil;
import cn.stylefeng.guns.modular.system.entity.Menu;
import cn.stylefeng.guns.modular.system.model.MenuDto;
import cn.stylefeng.guns.modular.system.service.MenuService;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.system.warpper.MenuWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@RestController
@RequestMapping("/gunsApi/menu")
public class MenuApiController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "通过token获取做成菜单", notes = "通过token获取做成菜单")
    @RequestMapping(value = "/left_menu", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    public ResponseData leftMenu(@RequestParam String token) {

        //String token = getHttpServletRequest().getHeader("token");
        if (ToolUtil.isEmpty(token)) {
            throw new ServiceException(BizExceptionEnum.TOKEN_ERROR);
        }
        //根据token获取userId
        String userId = JwtTokenUtil.getUsernameFromToken(token);

        return ResultData.success(userService.getLeftMenuByUserId(userId));
    }


    /**
     * 修该菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改菜单", key = "name", dict = MenuDict.class)
    public ResponseData edit(MenuDto menu) {

        //如果修改了编号，则该菜单的子菜单也要修改对应编号
        this.menuService.updateMenu(menu);

        //刷新当前用户菜单
        //  this.userService.refreshCurrentUser();

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(@RequestParam(required = false) String menuName,
                             @RequestParam(required = false) String level,
                             @RequestParam(required = false) Long menuId) {
        Page<Map<String, Object>> menus = this.menuService.selectMenus(menuName, level, menuId);
        Page<Map<String, Object>> wrap = new MenuWrapper(menus).wrap();
        return ResultData.success(wrap);
    }

    /**
     * 获取菜单列表（s树形）
     *
     * @author fengshuonan
     * @Date 2019年2月23日22:01:47
     */
    @ApiOperation(value = "获取菜单列表（s树形）", notes = "获取菜单列表（s树形）")
    @RequestMapping(value = "/listTree", method = RequestMethod.GET)
    public ResponseData listTree(@RequestParam(required = false) String menuName,
                                 @RequestParam(required = false) String level) {
        List<Map<String, Object>> menus = this.menuService.selectMenuTree(menuName, level);
        List<Map<String, Object>> menusWrap = new MenuWrapper(menus).wrap();

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menusWrap);
        return ResultData.success(result);
    }

    /**
     * 新增菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @BussinessLog(value = "菜单新增", key = "name", dict = MenuDict.class)
    public ResponseData add(MenuDto menu) {
        this.menuService.addMenu(menu);
        return SUCCESS_TIP;
    }

    /**
     * 删除菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @RequestMapping(value = "/{menuId}/remove", method = RequestMethod.DELETE)
    @BussinessLog(value = "删除菜单", key = "menuId", dict = DeleteDict.class)
    public ResponseData remove(@PathVariable Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(menuId));

        this.menuService.delMenuContainSubMenus(menuId);

        return SUCCESS_TIP;
    }

    /**
     * 查看菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "查看菜单", notes = "查看菜单")
    @RequestMapping(value = "/{menuId}/view", method = RequestMethod.GET)
    public ResponseData view(@PathVariable Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Menu menu = this.menuService.getById(menuId);
        return ResultData.success(menu);
    }

    /**
     * 获取菜单信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @ApiOperation(value = "获取菜单信息", notes = "获取菜单信息")
    @RequestMapping(value = "/{menuId}/getMenuInfo", method = RequestMethod.GET)
    public ResponseData getMenuInfo(@PathVariable Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        Menu menu = this.menuService.getById(menuId);

        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);

        //设置pid和父级名称
        menuDto.setPid(ConstantFactory.me().getMenuIdByCode(menuDto.getPcode()));
        menuDto.setPcodeName(ConstantFactory.me().getMenuNameByCode(menuDto.getPcode()));

        return ResultData.success(menuDto);
    }

    /**
     * 获取菜单列表(首页用)
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @ApiOperation(value = "获取菜单列表(首页用)", notes = "获取菜单列表(首页用)")
    @RequestMapping(value = "/menuTreeList", method = RequestMethod.GET)
    public ResponseData menuTreeList() {
        return ResultData.success(menuService.menuTreeList());
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @ApiOperation(value = "获取菜单列表(选择父级菜单用)", notes = "获取菜单列表(选择父级菜单用)")
    @RequestMapping(value = "/selectMenuTreeList", method = RequestMethod.GET)
    public ResponseData selectMenuTreeList() {
        List<AntTreeNode> roleTreeList = new ArrayList<>();
        roleTreeList.add(AntTreeNode.createParent());
        roleTreeList.addAll(this.menuService.antTreeList());
        return ResultData.success(roleTreeList);
    }

    /**
     * 获取角色的菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @ApiOperation(value = "获取角色的菜单列表", notes = "获取角色的菜单列表")
    @RequestMapping(value = "/{roleId}/menuTreeListByRoleId", method = RequestMethod.GET)
    public ResponseData menuTreeListByRoleId(@PathVariable Long roleId) {
        List<Long> menuIds = this.menuService.getMenuIdsByRoleId(roleId);
        if (ToolUtil.isEmpty(menuIds)) {
            return ResultData.success(menuService.menuTreeList());
        } else {
            return ResultData.success(menuService.menuTreeListByMenuIds(menuIds));
        }
    }

}
