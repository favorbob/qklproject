package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/29 14:59:43
*/
public class EthSysAssetFlow {
    /**
    * 字段：eth_sys_asset_flow.id；备注：资产交易流水表
     */
    private String id;

    /**
    * 字段：eth_sys_asset_flow.trade_no；备注：交易号
     */
    private String tradeNo;

    /**
    * 字段：eth_sys_asset_flow.user_id；备注：uid
     */
    private String userId;

    /**
    * 字段：eth_sys_asset_flow.trade_amount；备注：交易金额
     */
    private BigDecimal tradeAmount;

    /**
    * 字段：eth_sys_asset_flow.remain_amount；备注：剩余金额
     */
    private BigDecimal remainAmount;

    /**
    * 字段：eth_sys_asset_flow.trade_type；备注：流水类型_flow_type
     */
    private Integer tradeType;

    /**
    * 字段：eth_sys_asset_flow.business_type；备注：业务类型
     */
    private Integer businessType;

    /**
    * 字段：eth_sys_asset_flow.is_plus；备注：收入，支出
     */
    private Integer isPlus;

    /**
    * 字段：eth_sys_asset_flow.create_time；备注：交易时间
     */
    private Date createTime;

    /**
    * 字段：eth_sys_asset_flow.transaction_hash；备注：交易hash
     */
    private String transactionHash;

    /**
    * 字段：eth_sys_asset_flow.at_block_number；备注：区块高度
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

    public Integer getIsPlus() {
        return isPlus;
    }

    public void setIsPlus(Integer isPlus) {
        this.isPlus = isPlus;
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