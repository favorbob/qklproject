package cc.stbl.token.innerdisc.modules.centerManager.entity;

import java.util.Date;

/**
 * @author caojinbo
 * 客服图片
 */
public class CenterEditServicePic {
    private String id;

    private String editServiceId;

    private String serviceName;

    private String picUrl;

    private Date createDate;

    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getEditServiceId() {
        return editServiceId;
    }

    public void setEditServiceId(String editServiceId) {
        this.editServiceId = editServiceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}