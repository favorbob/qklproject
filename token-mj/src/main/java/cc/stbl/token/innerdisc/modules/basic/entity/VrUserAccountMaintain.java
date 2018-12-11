package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

public class VrUserAccountMaintain {
    private String id;

    private String userId;

    private Date createDate;

    private Integer acType;

    private String beforeModife;

    private String afterModife;

    private String remark;

    private String phoneNum; //会员编号
    //query view
    private Date startDate;
    private Date endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getBeforeModife() {
        return beforeModife;
    }

    public void setBeforeModife(String beforeModife) {
        this.beforeModife = beforeModife;
    }

    public String getAfterModife() {
        return afterModife;
    }

    public void setAfterModife(String afterModife) {
        this.afterModife = afterModife;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}