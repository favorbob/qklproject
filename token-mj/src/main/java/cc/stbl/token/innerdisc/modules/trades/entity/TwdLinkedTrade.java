package cc.stbl.token.innerdisc.modules.trades.entity;

import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import ss.stbl.common.datastore.domain.OrderByQuery;

import java.util.Date;

/**
* create by framework, create date 2018/08/22 11:20:15
*/
public class TwdLinkedTrade extends OrderByQuery{
    /**
    * 字段：twd_linked_trade.id；备注：
     */
    private String id;

    /**
    * 字段：twd_linked_trade.linked_type；备注：挂靠类型,1:买入,2:卖出
     */
    private Integer linkedType;

    /**
    * 字段：twd_linked_trade.user_id；备注：
     */
    private String userId;

    /**
    * 字段：twd_linked_trade.asset_count；备注：资产数量
     */
    private Integer assetCount;

    /**
    * 字段：twd_linked_trade.single_price；备注：资产单价
     */
    private Long singlePrice;

    /**
    * 字段：twd_linked_trade.traded_count；备注：可交易的数量
     */
    private Integer tradedCount;

    /**
    * 字段：twd_linked_trade.linked_status；备注：挂靠状态;1处理中，2：挂单成功,3：撤销
     */
    private Integer linkedStatus;

    /**
    * 字段：twd_linked_trade.create_date；备注：创建时间
     */
    private Date createDate;

    /**
    * 字段：twd_linked_trade.update_date；备注：更新时间
     */
    private Date updateDate;

    /**
     * 字段：twd_linked_trade.system_linked；备注：系统挂单,0: 否 ,1是
     */
    private Integer systemLinked;

    private Integer confirmCount;
    /**
     * 交易类型 2：AIIC 4:MJ
     */
    private  Integer balanceType;

    /**
     * 交易hash
     */
    private  String transactionHash;

    /**
     * 字段：twd_linked_trade.charge_code；备注：收款码
     */
    private String chargeCode;

    private String startTime;

    private String endTime;

    public Integer getConfirmCount() {
        return confirmCount;
    }

    public void setConfirmCount(Integer confirmCount) {
        this.confirmCount = confirmCount;
    }

    private TwdLinkedTradeRecord tradeRecord;

    private EthTradeRecord ethTradeRecord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLinkedType() {
        return linkedType;
    }

    public void setLinkedType(Integer linkedType) {
        this.linkedType = linkedType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(Integer assetCount) {
        this.assetCount = assetCount;
    }

    public Long getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(Long singlePrice) {
        this.singlePrice = singlePrice;
    }

    public Integer getTradedCount() {
        return tradedCount;
    }

    public void setTradedCount(Integer tradedCount) {
        this.tradedCount = tradedCount;
    }

    public Integer getLinkedStatus() {
        return linkedStatus;
    }

    public void setLinkedStatus(Integer linkedStatus) {
        this.linkedStatus = linkedStatus;
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

    public Integer getSystemLinked() {
        return systemLinked;
    }

    public void setSystemLinked(Integer systemLinked) {
        this.systemLinked = systemLinked;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public TwdLinkedTradeRecord getTradeRecord() {
        return tradeRecord;
    }

    public void setTradeRecord(TwdLinkedTradeRecord tradeRecord) {
        this.tradeRecord = tradeRecord;
    }

    public Integer getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Integer balanceType) {
        this.balanceType = balanceType;
    }

    public EthTradeRecord getEthTradeRecord() {
        return ethTradeRecord;
    }

    public void setEthTradeRecord(EthTradeRecord ethTradeRecord) {
        this.ethTradeRecord = ethTradeRecord;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}