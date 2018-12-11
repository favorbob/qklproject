package cc.stbl.token.innerdisc.modules.basic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ss.stbl.common.datastore.domain.OrderByQuery;

import java.util.Date;

@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class VrUserInfo extends OrderByQuery{
	public static final Integer USER_STATUS_INACTIVE= 0;
    public static final Integer USER_STATUS_FREEZE = 2;
    public static final Integer USER_STATUS_NORMAL = 1;
    public static final Integer USER_STATUS_DELETE = -1;
    public static final Integer USER_LEVEL_SYS = 1;
    public static final Integer USER_LEVEL_TEAMLEADER = 2;
    public static final Integer USER_LEVEL_GENERAL = 9;
    
    public static final String USER_AREA_LEVEL_V1 = "V1";
    public static final String USER_AREA_LEVEL_V2 = "V2";
    public static final String USER_AREA_LEVEL_V3 = "V3";
    public static final String USER_AREA_LEVEL_V4 = "V4";
    public static final String USER_AAREA = "A";
    public static final String USER_BAREA = "B";
    public static final String TOP_USER = "A0000000000"; 
    public static final Integer TOP_USER_USER_CODE_LEVEL = 1; 
    public static final String TOP_USER_USER_CODE = "1"; 
    
    private String userId;

    private String phoneNum;

    private Integer userLevel;

    private String password;

    private String salt;

    private String userName;

    private String userIcon;

    private Integer status;

    private String inviteCode;

    private Date createDate;

    private Date updateDate;

    private String idCard;
    
    //支付密码
    private String payPassword;
    
    //设备号
    private String deviceId;
    
    //用户序号，用于顶层用户code 默认都是1
    private Integer seq=1;
    
    //用户所在层级
    private Integer userCodeLevel;
    
    //用户层级关系code
    private String userCode;
    
    //推荐人
    private String registerPhoneNum;
    //接点人
    private String nodePhoneNum;
    //A区值
    private Long aArea;
    //B区值
    private Long bArea;
    private String userAreaLevel;
    private String remark;
    private String freeze;
    private String unfreeze;
    private String newPassword;
    

    //by view
    private String integral;
    private String totalBalance;
    private VrUserInfo parent;
    private String parentPhoneNum;
    private String parentUserId;
    private String beginTime;
    private String endTime;

    private String activeCode;  //激活码账户-激活卡
    private String propertyAccount; //资产
    private String mjAccount;   //mj账户
    private String aiicAccount; //acci账户
    private String area;//区域
    
    private VrUserInfo leftNode;//左边节点
    private VrUserInfo rightNode;//右边节点
    

    
    
	public VrUserInfo getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(VrUserInfo leftNode) {
		this.leftNode = leftNode;
	}

	public VrUserInfo getRightNode() {
		return rightNode;
	}

	public void setRightNode(VrUserInfo rightNode) {
		this.rightNode = rightNode;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public VrUserInfo getParent() {
        return parent;
    }

    public void setParent(VrUserInfo parent) {
        this.parent = parent;
    }

    public String getParentPhoneNum() {
        return parentPhoneNum;
    }

    public void setParentPhoneNum(String parentPhoneNum) {
        this.parentPhoneNum = parentPhoneNum;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getUserCodeLevel() {
		return userCodeLevel;
	}

	public void setUserCodeLevel(Integer userCodeLevel) {
		this.userCodeLevel = userCodeLevel;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRegisterPhoneNum() {
		return registerPhoneNum;
	}

	public void setRegisterPhoneNum(String registerPhoneNum) {
		this.registerPhoneNum = registerPhoneNum;
	}

	public String getNodePhoneNum() {
		return nodePhoneNum;
	}

	public void setNodePhoneNum(String nodePhoneNum) {
		this.nodePhoneNum = nodePhoneNum;
	}

	public Long getaArea() {
		return aArea;
	}

	public void setaArea(Long aArea) {
		this.aArea = aArea;
	}

	public Long getbArea() {
		return bArea;
	}

	public void setbArea(Long bArea) {
		this.bArea = bArea;
	}

	public String getUserAreaLevel() {
		return userAreaLevel;
	}

	public void setUserAreaLevel(String userAreaLevel) {
		this.userAreaLevel = userAreaLevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}

	public String getUnfreeze() {
		return unfreeze;
	}

	public void setUnfreeze(String unfreeze) {
		this.unfreeze = unfreeze;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getPropertyAccount() {
        return propertyAccount;
    }

    public void setPropertyAccount(String propertyAccount) {
        this.propertyAccount = propertyAccount;
    }

    public String getMjAccount() {
        return mjAccount;
    }

    public void setMjAccount(String mjAccount) {
        this.mjAccount = mjAccount;
    }

    public String getAiicAccount() {
        return aiicAccount;
    }

    public void setAiicAccount(String aiicAccount) {
        this.aiicAccount = aiicAccount;
    }

    @Override
	public String toString() {
		return "VrUserInfo [userId=" + userId + ", phoneNum=" + phoneNum + ", userLevel=" + userLevel + ", password="
				+ password + ", salt=" + salt + ", userName=" + userName + ", userIcon=" + userIcon + ", status="
				+ status + ", inviteCode=" + inviteCode + ", createDate=" + createDate + ", updateDate=" + updateDate
				+ ", idCard=" + idCard + ", payPassword=" + payPassword + ", deviceId=" + deviceId + ", seq=" + seq
				+ ", userCodeLevel=" + userCodeLevel + ", userCode=" + userCode + ", registerPhoneNum="
				+ registerPhoneNum + ", nodePhoneNum=" + nodePhoneNum + ", aArea=" + aArea + ", bArea=" + bArea
				+ ", userAreaLevel=" + userAreaLevel + ", remark=" + remark + ", freeze=" + freeze + ", unfreeze="
				+ unfreeze + ", newPassword=" + newPassword + ", integral=" + integral + ", totalBalance="
				+ totalBalance + ", parent=" + parent + ", parentPhoneNum=" + parentPhoneNum + ", parentUserId="
				+ parentUserId + ", beginTime=" + beginTime + ", endTime=" + endTime + "]";
	}
	
	
}