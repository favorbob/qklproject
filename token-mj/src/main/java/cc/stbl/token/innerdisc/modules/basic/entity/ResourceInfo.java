package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/20 18:00:12
*/
public class ResourceInfo {
    /**
    * 字段：resource_info.id；备注：
     */
    private String id;

    /**
    * 字段：resource_info.resource_name；备注：资源名称
     */
    private String resourceName;

    /**
    * 字段：resource_info.resource_type；备注：资源类型
     */
    private Integer resourceType;

    /**
    * 字段：resource_info.resource_logo；备注：资源logo
     */
    private String resourceLogo;

    /**
    * 字段：resource_info.resource_describe；备注：资源简介
     */
    private String resourceDescribe;

    /**
    * 字段：resource_info.upload_time；备注：上传时间
     */
    private Date uploadTime;

    /**
    * 字段：resource_info.resource_state；备注：资源状态
     */
    private Integer resourceState;

    /**
    * 字段：resource_info.user_id；备注：
     */
    private String userId;

    /**
    * 字段：resource_info.small_change_mining_ratio；备注：零钱购买挖矿比例
     */
    private Float smallChangeMiningRatio;

    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceLogo() {
        return resourceLogo;
    }

    public void setResourceLogo(String resourceLogo) {
        this.resourceLogo = resourceLogo;
    }

    public String getResourceDescribe() {
        return resourceDescribe;
    }

    public void setResourceDescribe(String resourceDescribe) {
        this.resourceDescribe = resourceDescribe;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getSmallChangeMiningRatio() {
        return smallChangeMiningRatio;
    }

    public void setSmallChangeMiningRatio(Float smallChangeMiningRatio) {
        this.smallChangeMiningRatio = smallChangeMiningRatio;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ResourceInfo{" +
                "id='" + id + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceType=" + resourceType +
                ", resourceLogo='" + resourceLogo + '\'' +
                ", resourceDescribe='" + resourceDescribe + '\'' +
                ", uploadTime=" + uploadTime +
                ", resourceState=" + resourceState +
                ", userId='" + userId + '\'' +
                ", smallChangeMiningRatio=" + smallChangeMiningRatio +
                ", price=" + price +
                '}';
    }
}