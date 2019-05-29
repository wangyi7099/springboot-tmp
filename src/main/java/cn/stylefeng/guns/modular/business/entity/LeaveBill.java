package cn.stylefeng.guns.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("biz_leave_bill")
public class LeaveBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 申请人
     */
    @TableField("applicant")
    private Integer applicant;

    @TableField("app_time")
    private Date appTime;

    /**
     * 申请时间
     */
    @TableField("days")
    private Integer days;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicant() {
        return applicant;
    }

    public void setApplicant(Integer applicant) {
        this.applicant = applicant;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LeaveBill{" +
                "id=" + id +
                ", applicant=" + applicant +
                ", appTime=" + appTime +
                ", days=" + days +
                ", title=" + title +
                ", content=" + content +
                "}";
    }
}
