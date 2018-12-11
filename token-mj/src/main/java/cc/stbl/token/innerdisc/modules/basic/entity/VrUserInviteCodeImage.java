package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

/**
* create by framework, create date 2018/08/20 19:59:14
*/
public class VrUserInviteCodeImage {
    /**
    * 字段：vr_user_invite_code_image.id；备注：
     */
    private String id;

    /**
    * 字段：vr_user_invite_code_image.qr_code_img；备注：邀请码生成的二维码地址
     */
    private String qrCodeImg;

    /**
    * 字段：vr_user_invite_code_image.invite_code；备注：邀请码
     */
    private String inviteCode;

    /**
    * 字段：vr_user_invite_code_image.user_id；备注：
     */
    private String userId;

    /**
    * 字段：vr_user_invite_code_image.create_date；备注：创建时间
     */
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    @Override
    public String toString() {
        return "VrUserInviteCodeImage{" +
                "id='" + id + '\'' +
                ", qrCodeImg='" + qrCodeImg + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", userId='" + userId + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}