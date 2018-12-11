package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

import lombok.Data;
import ss.stbl.common.datastore.domain.OrderByQuery;

@Data
public class VrUserAssetUpdateLog extends OrderByQuery{

	private String id;
	private String userId;
	private String phoneNum;
	private String assetChangeBefore;
	private String assetChangeAfter;
	private Date createTime;
	private Integer assetType;

    //view 查询
    private Date beginTime;
    private Date endTime;

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAssetChangeBefore() {
        return assetChangeBefore;
    }

    public void setAssetChangeBefore(String assetChangeBefore) {
        this.assetChangeBefore = assetChangeBefore;
    }

    public String getAssetChangeAfter() {
        return assetChangeAfter;
    }

    public void setAssetChangeAfter(String assetChangeAfter) {
        this.assetChangeAfter = assetChangeAfter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
