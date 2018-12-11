package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EthIntegralAmplify {
    private String id;

    private String userId;

    private String address;

    private BigDecimal totalToken;

    private BigDecimal totalIntegral;

    private BigDecimal returnedIntegral;

    private Integer status;

    private Date createDate;

    private Long lastBlockNumber;

    private String transactionHash;

    private Long atBlockNumber;

    private Integer tokenType;

    private Integer optType;

    private String extArgs;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalToken() {
        return totalToken;
    }

    public void setTotalToken(BigDecimal totalToken) {
        this.totalToken = totalToken;
    }

    public BigDecimal getTotalIntegral() {
        return totalIntegral;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public void setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
    }

    public void setTotalIntegral(BigDecimal totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public BigDecimal getReturnedIntegral() {
        return returnedIntegral;
    }

    public void setReturnedIntegral(BigDecimal returnedIntegral) {
        this.returnedIntegral = returnedIntegral;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getLastBlockNumber() {
        return lastBlockNumber;
    }

    public void setLastBlockNumber(Long lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
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

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public String getExtArgs() {
        return extArgs;
    }

    public void setExtArgs(String extArgs) {
        this.extArgs = extArgs;
    }
}