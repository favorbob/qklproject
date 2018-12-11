package cc.stbl.token.innerdisc.modules.basic.entity;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.StatusEnum;

import java.util.Date;

/**
* create by framework, create date 2018/08/22 16:03:36
*/
public class VrUserLoginLog {
    /**
    * 字段：vr_user_login_log.id；备注：
     */
    private String id;

    /**
    * 字段：vr_user_login_log.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：vr_user_login_log.login_time；备注：登录时间
     */
    private Date loginTime;

    /**
     * 字段：vr_user_login_log.login_time；备注：登录状态 0正常 1异常
     */
    private Integer loginStatus;  //StatusEnum

    public VrUserLoginLog() {}

    public VrUserLoginLog(String userId,StatusEnum statusEnum) {
        this.id = JavaUUIDGenerator.get();
        this.userId = userId;
        this.loginTime = new Date();
        this.loginStatus = statusEnum.getCode();
    }

    public VrUserLoginLog(String userId, Date loginTime,StatusEnum statusEnum) {
        this.id = JavaUUIDGenerator.get();
        this.userId = userId;
        this.loginTime = loginTime;
        this.loginStatus = statusEnum.getCode();
    }

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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }
}