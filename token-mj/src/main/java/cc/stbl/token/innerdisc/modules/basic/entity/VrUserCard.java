package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

import ss.stbl.common.datastore.domain.OrderByQuery;

/**
 * 卡实体
 * @author fyf
 *
 */
public class VrUserCard extends OrderByQuery{

	public static Integer VR_USER_CARD_STATUS_0 = 0;//未使用
	public static Integer VR_USER_CARD_STATUS_1 = 1;//已经使用
	
	public static String VR_USER_CARD_CARD_TYPE_0 = "GS";//公司
	public static String VR_USER_CARD_CARD_TYPE_1 = "HY";//会员
	
	private String id;
	private String cardNo;
	private String userId;
	private String phoneNum;
	private String cardType;
	private Date updateTime;
	private Integer status;
	
	//view 查询
	private Date beginTime;
	private Date endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
