package cc.stbl.token.innerdisc.modules.basic.entity;

import java.io.Serializable;
import java.util.Date;

/**
* create by framework, create date 2018/08/22 18:10:59
*/
public class ResourceUserUsedTime implements Serializable {
    private static final long serialVersionUID = 5156990756796178791L;
    /**
    * 字段：resource_user_used_time.id；备注：
     */
    private String id;

    /**
    * 字段：resource_user_used_time.resource_id；备注：
     */
    private String resourceId;

    /**
    * 字段：resource_user_used_time.device_id；备注：
     */
    private String deviceId;

    /**
    * 字段：resource_user_used_time.user_id；备注：
     */
    private String userId;

    /**
    * 字段：resource_user_used_time.resource_type；备注：资源类型  1游戏/2电影
     */
    private Integer resourceType;

    /**
    * 字段：resource_user_used_time.start_time；备注：
     */
    private Date startTime;

    /**
    * 字段：resource_user_used_time.end_time；备注：
     */
    private Date endTime;

    /**
    * 字段：resource_user_used_time.times；备注：时长  秒
     */
    private Long times;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "ResourceUserUsedTime{" +
                "id='" + id + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", userId='" + userId + '\'' +
                ", resourceType=" + resourceType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", times=" + times +
                '}';
    }
}