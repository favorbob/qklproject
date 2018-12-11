package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EthTradeRecord {
    private String id;

    private String tradeNo;

    private Integer fromFlowType;
    private Integer toFlowType;

    private String fromAddress;

    private String fromUserId;

    private BigDecimal tradeAmount;

    private String toAddress;

    private String toUserId;

    private Date createDate;

    private Date successDate;

    private Integer tradeStatus;

    private Integer tradeType;

    private String transactionHash;

    private Long atBlockNumber;

    private Long lastBlockNumber;

    private String fromRemark;
    private String toRemark;

    //for query
    private String phoneNum;
    private Date startTradeDate;
    private Date entTradeDate;
    private Date createDateByDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFromFlowType() {
        return fromFlowType;
    }

    public void setFromFlowType(Integer fromFlowType) {
        this.fromFlowType = fromFlowType;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getToRemark() {
        return toRemark;
    }

    public void setToRemark(String toRemark) {
        this.toRemark = toRemark;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
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

    public Long getAtBlockNumber() {
        return atBlockNumber;
    }

    public void setAtBlockNumber(Long atBlockNumber) {
        this.atBlockNumber = atBlockNumber;
    }

    public Long getLastBlockNumber() {
        return lastBlockNumber;
    }

    public void setLastBlockNumber(Long lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
    }

    public String getFromRemark() {
        return fromRemark;
    }

    public void setFromRemark(String fromRemark) {
        this.fromRemark = fromRemark;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getStartTradeDate() {
        return startTradeDate;
    }

    public void setStartTradeDate(Date startTradeDate) {
        this.startTradeDate = startTradeDate;
    }

    public Date getEntTradeDate() {
        return entTradeDate;
    }

    public void setEntTradeDate(Date entTradeDate) {
        this.entTradeDate = entTradeDate;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getToFlowType() {
        return toFlowType;
    }

    public void setToFlowType(Integer toFlowType) {
        this.toFlowType = toFlowType;
    }

    public Date getCreateDateByDay() {
        return createDateByDay;
    }

    public void setCreateDateByDay(Date createDateByDay) {
        this.createDateByDay = createDateByDay;
    }
}