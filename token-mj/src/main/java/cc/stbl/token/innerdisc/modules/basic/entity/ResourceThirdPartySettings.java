package cc.stbl.token.innerdisc.modules.basic.entity;

/**
* create by framework, create date 2018/08/22 14:23:11
*/
public class ResourceThirdPartySettings {
    /**
    * 字段：resource_third_party_settings.id；备注：
     */
    private String id;

    /**
    * 字段：resource_third_party_settings.free_use_time；备注：观看免费时长 秒
     */
    private Integer freeUseTime;

    /**
    * 字段：resource_third_party_settings.resource_type；备注：资源类型  0电影/1游戏
     */
    private Integer resourceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFreeUseTime() {
        return freeUseTime;
    }

    public void setFreeUseTime(Integer freeUseTime) {
        this.freeUseTime = freeUseTime;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return "ResourceThirdPartySettings{" +
                "id='" + id + '\'' +
                ", freeUseTime=" + freeUseTime +
                ", resourceType=" + resourceType +
                '}';
    }
}