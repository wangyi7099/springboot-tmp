package cn.stylefeng.guns.modular.business.service;

import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.business.entity.LeaveBill;
import cn.stylefeng.guns.modular.business.model.params.LeaveBillParam;
import cn.stylefeng.guns.modular.business.model.result.LeaveBillResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yaoliguo
 * @since 2019-05-27
 */
public interface LeaveBillService extends IService<LeaveBill> {

    /**
     * 新增
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    void add(LeaveBillParam param);

    /**
     * 删除
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    void delete(LeaveBillParam param);

    /**
     * 更新
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    void update(LeaveBillParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    LeaveBillResult findBySpec(LeaveBillParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    List<LeaveBillResult> findListBySpec(LeaveBillParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author yaoliguo
     * @Date 2019-05-27
     */
    LayuiPageInfo findPageBySpec(LeaveBillParam param);

}
