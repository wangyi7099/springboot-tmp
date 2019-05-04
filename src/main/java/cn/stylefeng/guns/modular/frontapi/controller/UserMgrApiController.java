package cn.stylefeng.guns.modular.frontapi.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.constant.dictmap.UserDict;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.constant.state.ManagerStatus;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.factory.UserFactory;
import cn.stylefeng.guns.modular.system.model.UserDto;
import cn.stylefeng.guns.modular.system.service.UserService;
import cn.stylefeng.guns.modular.system.warpper.UserWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/gunsApi/mgr")
public class UserMgrApiController extends BaseController {

    @Autowired
    private GunsProperties gunsProperties;

    @Autowired
    private UserService userService;


    /**
     * 获取用户详情
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "获取用户详情", notes = "获取用户详情")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseData getUserInfo(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new RequestEmptyException();
        }

        this.userService.assertAuth(userId);
        User user = this.userService.getById(userId);
        Map<String, Object> map = UserFactory.removeUnSafeFields(user);

        HashMap<Object, Object> hashMap = CollectionUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));

        return ResponseData.success(hashMap);
    }

    /**
     * 修改当前用户的密码
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "修改当前用户的密码", notes = "修改当前用户的密码")
    @RequestMapping(value = "/changePwd", method = RequestMethod.PUT)
    public ResponseData changePwd(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        if (ToolUtil.isOneEmpty(oldPassword, newPassword)) {
            throw new RequestEmptyException();
        }
        this.userService.changePwd(oldPassword, newPassword);
        return SUCCESS_TIP;
    }

    /**
     * 查询管理员列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "查询管理员列表", notes = "查询管理员列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String timeLimit,
                             @RequestParam(required = false) Long deptId,
                             @RequestParam(required = true) Integer limit,
                             @RequestParam(required = true) Integer page) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }


        Page<Map<String, Object>> users = userService.selectUsers(null, name, beginTime, endTime, deptId);
        Page wrapped = new UserWrapper(users).wrap();
        return ResultData.success(wrapped);
    }

    /**
     * 添加管理员
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "添加管理员", notes = "添加管理员")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
    public ResponseData add(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.addUser(user);
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "添加管理员", notes = "添加管理员")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    public ResponseData edit(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.editUser(user);
        return SUCCESS_TIP;
    }

    /**
     * 删除管理员（逻辑删除）
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "删除管理员", notes = "删除管理员")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    public ResponseData delete(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.deleteUser(userId);
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "查看管理员详情", notes = "查看管理员详情")
    @RequestMapping(value = "/{userId}/view", method = RequestMethod.GET)
    public ResponseData view(@PathVariable Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        userService.assertAuth(userId);
        return ResponseData.success(userService.getById(userId));
    }

    /**
     * 重置管理员的密码
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "重置管理员的密码", notes = "重置管理员的密码")
    @RequestMapping(value = "/{userId}/reset", method = RequestMethod.PUT)
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
    public ResponseData reset(@PathVariable Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        User user = this.userService.getById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 冻结用户
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "冻结用户", notes = "冻结用户")
    @RequestMapping(value = "/{userId}/freeze", method = RequestMethod.PUT)
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
    public ResponseData freeze(@PathVariable Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        this.userService.assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "解除冻结用户", notes = "解除冻结用户")
    @RequestMapping(value = "/{userId}/unfreeze", method = RequestMethod.PUT)
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    public ResponseData unfreeze(@PathVariable Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "分配角色", notes = "分配角色")
    @RequestMapping(value = "/{userId}/setRole", method = RequestMethod.PUT)
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
    public ResponseData setRole(@PathVariable("userId") Long userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        this.userService.assertAuth(userId);
        this.userService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 上传图片
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @ApiOperation(value = "上传图片", notes = "上传图片")
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    public ResponseData upload(@RequestPart("file") MultipartFile picture) {

        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return ResponseData.success(pictureName);
    }

}
