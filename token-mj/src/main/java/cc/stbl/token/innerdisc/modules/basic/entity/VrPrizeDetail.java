package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

import ss.stbl.common.datastore.domain.OrderByQuery;

/**
* create by framework, create date 2018/09/27 10:50:19
*/
public class VrPrizeDetail extends OrderByQuery{
    /**
    * 字段：vr_prize_detail.id；备注：
     */
    private String id;

    /**
    * 字段：vr_prize_detail.user_id；备注：
     */
    private String userId;

    /**
    * 字段：vr_prize_detail.total_earning；备注：总静态收益
     */
    private BigDecimal totalEarning;

    /**
    * 字段：vr_prize_detail.mj_earning；备注：mj静态收益
     */
    private BigDecimal mjEarning;

    /**
    * 字段：vr_prize_detail.aiic_earning；备注：AIIC静态收益
     */
    private BigDecimal aiicEarning;

    /**
    * 字段：vr_prize_detail.level_award；备注：层级奖励
     */
    private BigDecimal levelAward;

    /**
    * 字段：vr_prize_detail.original_asset；备注：原资产账户
     */
    private BigDecimal originalAsset;

    /**
    * 字段：vr_prize_detail.after_asset；备注：改变后资产账户
     */
    private BigDecimal afterAsset;

    /**
    * 字段：vr_prize_detail.original_mj；备注：原mj
     */
    private BigDecimal originalMj;

    /**
    * 字段：vr_prize_detail.after_mj；备注：改变后mj
     */
    private BigDecimal afterMj;

    /**
    * 字段：vr_prize_detail.original_aiic；备注：原AIIC
     */
    private BigDecimal originalAiic;

    /**
    * 字段：vr_prize_detail.after_aiic；备注：改变后AIIC
     */
    private BigDecimal afterAiic;

    /**
    * 字段：vr_prize_detail.settle_date；备注：结算日期,用户领取红包的时间
     */
    private Date settleDate;

    /**
    * 字段：vr_prize_detail.settle_count；备注：结算次数,红包领取次数，实际只可能是0，1
     */
    private Boolean settleCount;
    
	/**
	 * 字段：vr_prize_detail.create_date；备注：红包生成时间
	 */
	private String createDate;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	private BigDecimal mj;
    private BigDecimal aiic;
    
    //开始的查询时间
    private Date beginTime;
    //结束的查询时间
    private Date endTime;
    private String phoneNum;    //奖金明细查询返回字段
    private String userLevel;  //奖金明细查询返回字段
    private String userName;    //奖金明细查询返回字段
    private String settleDateStr;    //奖金明细查询返回字段
    
    

    public String getSettleDateStr() {
		return settleDateStr;
	}

	public void setSettleDateStr(String settleDateStr) {
		this.settleDateStr = settleDateStr;
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

    public BigDecimal getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(BigDecimal totalEarning) {
        this.totalEarning = totalEarning;
    }

    public BigDecimal getMjEarning() {
        return mjEarning;
    }

    public void setMjEarning(BigDecimal mjEarning) {
        this.mjEarning = mjEarning;
    }

    public BigDecimal getAiicEarning() {
        return aiicEarning;
    }

    public void setAiicEarning(BigDecimal aiicEarning) {
        this.aiicEarning = aiicEarning;
    }

    public BigDecimal getLevelAward() {
        return levelAward;
    }

    public void setLevelAward(BigDecimal levelAward) {
        this.levelAward = levelAward;
    }

    public BigDecimal getOriginalAsset() {
        return originalAsset;
    }

    public void setOriginalAsset(BigDecimal originalAsset) {
        this.originalAsset = originalAsset;
    }

    public BigDecimal getAfterAsset() {
        return afterAsset;
    }

    public void setAfterAsset(BigDecimal afterAsset) {
        this.afterAsset = afterAsset;
    }

    public BigDecimal getOriginalMj() {
        return originalMj;
    }

    public void setOriginalMj(BigDecimal originalMj) {
        this.originalMj = originalMj;
    }

    public BigDecimal getAfterMj() {
        return afterMj;
    }

    public void setAfterMj(BigDecimal afterMj) {
        this.afterMj = afterMj;
    }

    public BigDecimal getOriginalAiic() {
        return originalAiic;
    }

    public void setOriginalAiic(BigDecimal originalAiic) {
        this.originalAiic = originalAiic;
    }

    public BigDecimal getAfterAiic() {
        return afterAiic;
    }

    public void setAfterAiic(BigDecimal afterAiic) {
        this.afterAiic = afterAiic;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public Boolean getSettleCount() {
        return settleCount;
    }

    public void setSettleCount(Boolean settleCount) {
        this.settleCount = settleCount;
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

    public BigDecimal getMj() {
		return mj;
	}

	public void setMj(BigDecimal mj) {
		this.mj = mj;
	}

	public BigDecimal getAiic() {
		return aiic;
	}

	public void setAiic(BigDecimal aiic) {
		this.aiic = aiic;
	}

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}