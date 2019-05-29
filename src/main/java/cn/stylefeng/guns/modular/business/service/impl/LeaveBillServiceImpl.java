package cn.stylefeng.guns.modular.business.service.impl;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.common.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.business.entity.LeaveBill;
import cn.stylefeng.guns.modular.business.mapper.LeaveBillMapper;
import cn.stylefeng.guns.modular.business.model.params.LeaveBillParam;
import cn.stylefeng.guns.modular.business.model.result.LeaveBillResult;
import cn.stylefeng.guns.modular.business.service.LeaveBillService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yaoliguo
 * @since 2019-05-27
 */
@Service
public class LeaveBillServiceImpl extends ServiceImpl<LeaveBillMapper, LeaveBill> implements LeaveBillService {

    @Override
    public void add(LeaveBillParam param) {
        LeaveBill entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(LeaveBillParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(LeaveBillParam param) {
        LeaveBill oldEntity = getOldEntity(param);
        LeaveBill newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public LeaveBillResult findBySpec(LeaveBillParam param) {
        return null;
    }

    @Override
    public List<LeaveBillResult> findListBySpec(LeaveBillParam param) {
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(LeaveBillParam param) {
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(LeaveBillParam param) {
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private LeaveBill getOldEntity(LeaveBillParam param) {
        return this.getById(getKey(param));
    }

    private LeaveBill getEntity(LeaveBillParam param) {
        LeaveBill entity = new LeaveBill();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
