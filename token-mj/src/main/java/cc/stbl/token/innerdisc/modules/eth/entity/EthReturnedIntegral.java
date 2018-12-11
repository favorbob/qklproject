package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EthReturnedIntegral {
    private String id;

    private String userId;

    private String address;

    private String contractAt;

    private String transactionHash;

    private Long atBlockNumber;

    private Date optDate;

    private BigDecimal integral;

    private BigDecimal limitedBalance;

    private BigDecimal balance;

    private Date successDate;

    private Integer status;

    private String amplifyId;

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

    public String getContractAt() {
        return contractAt;
    }

    public void setContractAt(String contractAt) {
        this.contractAt = contractAt;
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

    public Date getOptDate() {
        return optDate;
    }

    public void setOptDate(Date optDate) {
        this.optDate = optDate;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public BigDecimal getLimitedBalance() {
        return limitedBalance;
    }

    public void setLimitedBalance(BigDecimal limitedBalance) {
        this.limitedBalance = limitedBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAmplifyId() {
        return amplifyId;
    }

    public void setAmplifyId(String amplifyId) {
        this.amplifyId = amplifyId;
    }

	@Override
	public String toString() {
		return "EthReturnedIntegral [id=" + id + ", userId=" + userId + ", address=" + address + ", contractAt="
				+ contractAt + ", transactionHash=" + transactionHash + ", atBlockNumber=" + atBlockNumber
				+ ", optDate=" + optDate + ", integral=" + integral + ", limitedBalance=" + limitedBalance
				+ ", balance=" + balance + ", successDate=" + successDate + ", status=" + status + ", amplifyId="
				+ amplifyId + "]";
	}
    
}