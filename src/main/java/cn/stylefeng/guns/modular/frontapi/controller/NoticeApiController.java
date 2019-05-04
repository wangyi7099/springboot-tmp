package cn.stylefeng.guns.modular.frontapi.controller;

import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.dictmap.DeleteDict;
import cn.stylefeng.guns.core.common.constant.dictmap.NoticeMap;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.Notice;
import cn.stylefeng.guns.modular.system.service.NoticeService;
import cn.stylefeng.guns.modular.system.warpper.NoticeWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 通知控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@RestController
@RequestMapping("/gunsApi/notice")
public class NoticeApiController extends BaseController {

    @Autowired
    private NoticeService noticeService;


    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @ApiOperation(value = "获取通知列表", notes = "获取通知列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(String condition) {
        Page<Map<String, Object>> list = this.noticeService.list(condition);
        Page<Map<String, Object>> wrap = new NoticeWrapper(list).wrap();
        return ResultData.success(wrap);
    }

    /**
     * 新增通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @ApiOperation(value = "新增通知", notes = "新增通知")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @BussinessLog(value = "新增通知", key = "title", dict = NoticeMap.class)
    public ResponseData add(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getTitle(), notice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        notice.setCreateUser(ShiroKit.getUserNotNull().getId());
        notice.setCreateTime(new Date());
        this.noticeService.save(notice);
        return SUCCESS_TIP;
    }

    /**
     * 删除通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @ApiOperation(value = "删除通知", notes = "删除通知")
    @RequestMapping(value = "{noticeId}/delete", method = RequestMethod.DELETE)
    @BussinessLog(value = "删除通知", key = "noticeId", dict = DeleteDict.class)
    public ResponseData delete(@PathVariable Long noticeId) {

        //缓存通知名称
        LogObjectHolder.me().set(ConstantFactory.me().getNoticeTitle(noticeId));

        this.noticeService.removeById(noticeId);

        return SUCCESS_TIP;
    }

    /**
     * 修改通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @ApiOperation(value = "修改通知", notes = "修改通知")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @BussinessLog(value = "修改通知", key = "title", dict = NoticeMap.class)
    public ResponseData update(Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getNoticeId(), notice.getTitle(), notice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Notice old = this.noticeService.getById(notice.getNoticeId());
        old.setTitle(notice.getTitle());
        old.setContent(notice.getContent());
        this.noticeService.updateById(old);
        return SUCCESS_TIP;
    }

}
