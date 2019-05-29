package cn.stylefeng.guns.modular.business.model.params;

import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yaoliguo
 * @since 2019-05-27
 */
@Data
public class LeaveBillParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 申请人
     */
    private Integer applicant;

    private Date appTime;

    /**
     * 申请时间
     */
    private Integer days;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    @Override
    public String checkParam() {
        return null;
    }

}
