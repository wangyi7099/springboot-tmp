package cn.stylefeng.guns.modular.frontapi.controller;

import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.system.model.params.DictParam;
import cn.stylefeng.guns.modular.system.model.result.DictResult;
import cn.stylefeng.guns.modular.system.service.DictService;
import cn.stylefeng.guns.modular.system.service.DictTypeService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gunsApi/dict")
public class DictApiController extends BaseController {

    @Autowired
    private DictService dictService;

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
    public ResponseData addItem(DictParam dictParam) {
        this.dictService.add(dictParam);
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
    public ResponseData editItem(DictParam dictParam) {
        this.dictService.update(dictParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "删除接口", notes = "删除接口")
    @RequestMapping(value = "{dictId}/delete", method = RequestMethod.DELETE)
    public ResponseData delete(@PathVariable Long dictId) {
        DictParam dictParam = new DictParam();
        dictParam.setDictId(dictId);
        this.dictService.delete(dictParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "查看详情接口", notes = "查看详情接口")
    @RequestMapping(value = "{dictId}/detail", method = RequestMethod.GET)
    public ResponseData detail(@PathVariable Long dictId) {
        DictResult dictResult = this.dictService.dictDetail(dictId);
        return ResponseData.success(dictResult);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ApiOperation(value = "查看详情接口", notes = "查看详情接口")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public LayuiPageInfo list(DictParam dictParam) {
        return this.dictService.findPageBySpec(dictParam);
    }

    /**
     * 获取某个类型下字典树的列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @ApiOperation(value = "查看详情接口", notes = "查看详情接口")
    @RequestMapping(value = "/ztree", method = RequestMethod.GET)
    public List<ZTreeNode> ztree(@RequestParam("dictTypeId") Long dictTypeId, @RequestParam(value = "dictId", required = false) Long dictId) {
        return this.dictService.dictTreeList(dictTypeId, dictId);
    }

}
