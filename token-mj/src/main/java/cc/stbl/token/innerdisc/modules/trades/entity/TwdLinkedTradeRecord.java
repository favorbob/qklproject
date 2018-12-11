package cc.stbl.token.innerdisc.modules.trades.entity;

import ss.stbl.common.datastore.domain.OrderByQuery;

import java.util.Date;

/**
* create by framework, create date 2018/08/22 11:16:49
*/
public class TwdLinkedTradeRecord extends OrderByQuery{
    /**
    * 字段：twd_linked_trade_record.id；备注：
     */
    private String id;

    /**
    * 字段：twd_linked_trade_record.linked_id；备注：挂单id
     */
    private String linkedId;

    /**
    * 字段：twd_linked_trade_record.from_user_id；备注：交易发起用户
     */
    private String fromUserId;

    /**
    * 字段：twd_linked_trade_record.to_user_id；备注：交易接收用户
     */
    private String toUserId;

    /**
    * 字段：twd_linked_trade_record.success_date；备注：交易成交时间
     */
    private Date successDate;

    /**
    * 字段：twd_linked_trade_record.linked_type；备注：挂单类型
     */
    private Integer linkedType;

    /**
    * 字段：twd_linked_trade_record.asset_count；备注：交易资产数量
     */
    private Integer assetCount;

    /**
    * 字段：twd_linked_trade_record.single_amount；备注：交易单价
     */
    private Long singleAmount;

    /**
    * 字段：twd_linked_trade_record.total_amount；备注：交易总价
     */
    private Long totalAmount;

    /**
    * 字段：twd_linked_trade_record.trade_status；备注：订单状态
     */
    private Integer tradeStatus;

    /**
    * 字段：twd_linked_trade_record.create_date；备注：发起时间
     */
    private Date createDate;
    /**
    * 字段：twd_linked_trade_record.accepted；备注：受理状态0:未处理,1:申请交易,2:拒绝交易
     */
    private Integer accepted;
    /**
    * 字段：twd_linked_trade_record.system_linked
     */
    private Integer systemLinked;
    /**
    * 字段：twd_linked_trade_record.tip_status 提醒状态 0:未提醒，1：已支付提醒，2：提醒已处理
     */
    private Integer tipStatus;
    /**
     * 交易类型 2：AIIC 4:MJ
     */
    private  Integer balanceType;
    /**
     * 区块号
     */
    private Integer blockNumber;

    /**
     * 交易类型
     */
    private  Integer tradeType;

    /**
     * 交易hash
     */
    private  String transactionHash;
    /**
     * 卖家电话号码
     */
    private  String sellerPhoneNum;

    //query view
    private String phoneNum;
    private Date startDate;
    private Date endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Integer getLinkedType() {
        return linkedType;
    }

    public void setLinkedType(Integer linkedType) {
        this.linkedType = linkedType;
    }

    public Integer getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(Integer assetCount) {
        this.assetCount = assetCount;
    }

    public Long getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(Long singleAmount) {
        this.singleAmount = singleAmount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public Integer getSystemLinked() {
        return systemLinked;
    }

    public Integer getTipStatus() {
        return tipStatus;
    }

    public void setTipStatus(Integer tipStatus) {
        this.tipStatus = tipStatus;
    }

    public void setSystemLinked(Integer systemLinked) {
        this.systemLinked = systemLinked;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Integer balanceType) {
        this.balanceType = balanceType;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getSellerPhoneNum() {
        return sellerPhoneNum;
    }

    public void setSellerPhoneNum(String sellerPhoneNum) {
        this.sellerPhoneNum = sellerPhoneNum;
    }
}