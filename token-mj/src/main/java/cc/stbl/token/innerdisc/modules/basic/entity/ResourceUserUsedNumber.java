package cc.stbl.token.innerdisc.modules.basic.entity;

/**
* create by framework, create date 2018/08/22 18:10:59
*/
public class ResourceUserUsedNumber {
    /**
    * 字段：resource_user_used_number.id；备注：
     */
    private String id;

    /**
    * 字段：resource_user_used_number.resource_id；备注：
     */
    private String resourceId;

    /**
    * 字段：resource_user_used_number.device_id；备注：
     */
    private String deviceId;

    /**
    * 字段：resource_user_used_number.user_id；备注：
     */
    private String userId;

    /**
    * 字段：resource_user_used_number.resource_type；备注：资源类型  1游戏/2电影
     */
    private Integer resourceType;

    /**
    * 字段：resource_user_used_number.used_number；备注：
     */
    private Integer usedNumber;

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

    public Integer getUsedNumber() {
        return usedNumber;
    }

    public void setUsedNumber(Integer usedNumber) {
        this.usedNumber = usedNumber;
    }

    @Override
    public String toString() {
        return "ResourceUserUsedNumber{" +
                "id='" + id + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", userId='" + userId + '\'' +
                ", resourceType=" + resourceType +
                ", usedNumber=" + usedNumber +
                '}';
    }
}