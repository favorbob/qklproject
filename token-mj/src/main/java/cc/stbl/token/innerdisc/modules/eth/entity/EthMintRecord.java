package cc.stbl.token.innerdisc.modules.eth.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/20 17:30:47
*/
public class EthMintRecord {
    /**
    * 字段：eth_mint_record.id；备注：交易号
     */
    private String id;

    /**
    * 字段：eth_mint_record.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：eth_mint_record.mint_type；备注：业务类型
     */
    private Integer mintType;

    /**
    * 字段：eth_mint_record.mint_amount；备注：挖矿金额
     */
    private BigDecimal mintAmount;

    /**
    * 字段：eth_mint_record.mint_date；备注：时间
     */
    private Date mintDate;

    /**
    * 字段：eth_mint_record.mint_status；备注：状态，0：申请,2成功
     */
    private Integer mintStatus;

    /**
    * 字段：eth_mint_record.transaction_hash；备注：交易hash
     */
    private String transactionHash;

    /**
    * 字段：eth_mint_record.at_block_number；备注：交易区块
     */
    private Long atBlockNumber;

    /**
    * 字段：eth_mint_record.last_block_number；备注：申请时区块高度
     */
    private Long lastBlockNumber;

    /**
    * 字段：eth_mint_record.address；备注：钱包地址
     */
    private String address;

    /**
    * 字段：eth_mint_record.mint_success_date；备注：成功时间
     */
    private Date mintSuccessDate;

    /**
    * 字段：eth_mint_record.remark；备注：
     */
    private String remark;

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

    public Integer getMintType() {
        return mintType;
    }

    public void setMintType(Integer mintType) {
        this.mintType = mintType;
    }

    public BigDecimal getMintAmount() {
        return mintAmount;
    }

    public void setMintAmount(BigDecimal mintAmount) {
        this.mintAmount = mintAmount;
    }

    public Date getMintDate() {
        return mintDate;
    }

    public void setMintDate(Date mintDate) {
        this.mintDate = mintDate;
    }

    public Integer getMintStatus() {
        return mintStatus;
    }

    public void setMintStatus(Integer mintStatus) {
        this.mintStatus = mintStatus;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getMintSuccessDate() {
        return mintSuccessDate;
    }

    public void setMintSuccessDate(Date mintSuccessDate) {
        this.mintSuccessDate = mintSuccessDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}