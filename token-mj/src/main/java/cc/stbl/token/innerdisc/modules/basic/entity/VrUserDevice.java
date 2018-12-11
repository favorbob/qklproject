package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

/**
* create by framework, create date 2018/08/23 14:57:29
*/
public class VrUserDevice {
    /**
    * 字段：vr_user_device.id；备注：
     */
    private String id;

    /**
    * 字段：vr_user_device.code；备注：
     */
    private String code;

    /**
    * 字段：vr_user_device.name；备注：
     */
    private String name;

    /**
    * 字段：vr_user_device.user_id；备注：
     */
    private String userId;

    /**
    * 字段：vr_user_device.bind_time；备注：
     */
    private Date bindTime;

    /**
    * 字段：vr_user_device.bind_state；备注：
     */
    private Integer bindState;

    /**
    * 字段：vr_user_device.state；备注：
     */
    private Integer state;

    /**
    * 字段：vr_user_device.create_date；备注：
     */
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Integer getBindState() {
        return bindState;
    }

    public void setBindState(Integer bindState) {
        this.bindState = bindState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}