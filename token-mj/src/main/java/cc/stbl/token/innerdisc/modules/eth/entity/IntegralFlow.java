package cc.stbl.token.innerdisc.modules.eth.entity;

import ss.stbl.common.datastore.domain.OrderByQuery;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/17 17:39:58
*/
public class IntegralFlow extends OrderByQuery{
    /**
    * 字段：eth_integral_flow.id；备注：积分交易流水表
     */
    private String id;

    /**
    * 字段：eth_integral_flow.trade_no；备注：交易号
     */
    private String tradeNo;

    /**
    * 字段：eth_integral_flow.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：eth_integral_flow.trade_integral；备注：积分金额
     */
    private BigDecimal tradeIntegral;

    /**
    * 字段：eth_integral_flow.remain_integral；备注：剩余积分
     */
    private BigDecimal remainIntegral;

    /**
    * 字段：eth_integral_flow.trade_type；备注：交易类型，0收入，1支出
     */
    private Integer tradeType;

    /**
    * 字段：eth_integral_flow.business_type；备注：业务类型
     */
    private Integer businessType;

    /**
    * 字段：eth_integral_flow.create_time；备注：
     */
    private Date createTime;

    /**
    * 字段：eth_integral_flow.transaction_hash；备注：交易hash
     */
    private String transactionHash;

    /**
    * 字段：eth_integral_flow.at_block_number；备注：区块高度
     */
    private Long atBlockNumber;

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

    public BigDecimal getTradeIntegral() {
        return tradeIntegral;
    }

    public void setTradeIntegral(BigDecimal tradeIntegral) {
        this.tradeIntegral = tradeIntegral;
    }

    public BigDecimal getRemainIntegral() {
        return remainIntegral;
    }

    public void setRemainIntegral(BigDecimal remainIntegral) {
        this.remainIntegral = remainIntegral;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}