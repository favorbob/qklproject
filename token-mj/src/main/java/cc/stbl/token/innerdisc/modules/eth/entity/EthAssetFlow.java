package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EthAssetFlow {
    private String id;

    private String tradeNo;

    private String userId;

    private BigDecimal tradeAmount;

    private BigDecimal remainAmount;

    private Integer tradeType;

    private String tradeTypeName;

    private Integer businessType;

    private Boolean isPlus;

    private Date createTime;

    private String transactionHash;

    private Long atBlockNumber;

    private String remark;

    //for query
    private Date startTradeDate;
    private Date entTradeDate;
    private Date createDateByDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Boolean getIsPlus() {
        return isPlus;
    }

    public void setIsPlus(Boolean isPlus) {
        this.isPlus = isPlus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
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

    public Date getCreateDateByDay() {
        return createDateByDay;
    }

    public void setCreateDateByDay(Date createDateByDay) {
        this.createDateByDay = createDateByDay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}