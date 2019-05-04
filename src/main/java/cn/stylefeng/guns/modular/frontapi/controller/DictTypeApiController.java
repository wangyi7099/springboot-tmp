package cn.stylefeng.guns.modular.frontapi.controller;

import cn.stylefeng.guns.core.request.ResultData;
import cn.stylefeng.guns.modular.system.entity.DictType;
import cn.stylefeng.guns.modular.system.model.params.DictTypeParam;
import cn.stylefeng.guns.modular.system.service.DictTypeService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/gunsApi/dictType")
public class DictTypeApiController extends BaseController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "新增接口", notes = "新增接口")
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public ResponseData addItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.add(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "编辑接口", notes = "编辑接口")
    @RequestMapping(value = "/editItem", method = RequestMethod.PUT)
    public ResponseData editItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.update(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "删除接口", notes = "删除接口")
    @RequestMapping(value = "{dictTypeId}/delete", method = RequestMethod.DELETE)
    public ResponseData delete(@PathVariable Long dictTypeId) {
        DictTypeParam dictTypeParam = new DictTypeParam();
        dictTypeParam.setDictTypeId(dictTypeId);
        this.dictTypeService.delete(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping(value = "{dictTypeId}/detail", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户详情", notes = "获取用户详情")
    public ResponseData detail(@PathVariable Long dictTypeId) {
        DictType detail = this.dictTypeService.getById(dictTypeId);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "查询列表", notes = "查询列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(DictTypeParam dictTypeParam) {
        return ResultData.success(dictTypeService.findPageBySpec(dictTypeParam));
    }

}
