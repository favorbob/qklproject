package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

/**
* create by framework, create date 2018/08/28 11:33:27
*/
public class VrUserImg {
    /**
    * 字段：vr_user_img.id；备注：
     */
    private String id;

    /**
    * 字段：vr_user_img.user_id；备注：用户ID
     */
    private String userId;

    /**
    * 字段：vr_user_img.img_url；备注：图片url
     */
    private String imgUrl;

    /**
    * 字段：vr_user_img.img_type；备注：类型：1微信码/2支付宝码
     */
    private Integer imgType;

    /**
    * 字段：vr_user_img.status；备注：状态：0闲置/1使用中
     */
    private Integer status;

    /**
    * 字段：vr_user_img.create_date；备注：
     */
    private Date createDate;

    /**
    * 字段：vr_user_img.is_default；备注：是否默认：0否/1是
     */
    private Integer isDefault;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getImgType() {
        return imgType;
    }

    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}