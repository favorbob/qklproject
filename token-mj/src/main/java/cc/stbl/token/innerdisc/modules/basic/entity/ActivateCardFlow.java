package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

public class ActivateCardFlow {
	
	public final static String ACTIVATECARDFLOW_IN = "1";
	public final static String ACTIVATECARDFLOW_OUT = "2";
	public final static String ActivateCardFlow_ACTIVATE = "3";
	public final static String ActivateCardFlow_RECHARGE = "4";

	private String id;
	private String phoneNum;
	private Date createTime;
	private Integer changeBefore;
	private Integer changeNum;
	private Integer changeAfter;
	private String changeReason;
	private String remark;

	//query view
	private Date startDate;
	private Date endDate;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getChangeBefore() {
		return changeBefore;
	}
	public void setChangeBefore(Integer changeBefore) {
		this.changeBefore = changeBefore;
	}
	public Integer getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}
	public Integer getChangeAfter() {
		return changeAfter;
	}
	public void setChangeAfter(Integer changeAfter) {
		this.changeAfter = changeAfter;
	}
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
