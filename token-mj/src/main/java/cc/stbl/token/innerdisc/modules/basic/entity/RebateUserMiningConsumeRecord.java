package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/20 18:00:12
*/
public class RebateUserMiningConsumeRecord {
    /**
    * 字段：rebate_user_mining_consume_record.id；备注：
     */
    private String id;

    /**
    * 字段：rebate_user_mining_consume_record.user_id；备注：
     */
    private String userId;

    /**
    * 字段：rebate_user_mining_consume_record.mining_amount_sum；备注：挖矿金额累计
     */
    private BigDecimal miningAmountSum;

    /**
    * 字段：rebate_user_mining_consume_record.consumed_amount_sum；备注：消耗累计金额
     */
    private BigDecimal consumedAmountSum;

    /**
    * 字段：rebate_user_mining_consume_record.last_sum_time；备注：最近累计时间
     */
    private Date lastSumTime;

    /**
    * 字段：rebate_user_mining_consume_record.last_mining_time；备注：最近挖矿时间
     */
    private Date lastMiningTime;

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

    public BigDecimal getMiningAmountSum() {
        return miningAmountSum;
    }

    public void setMiningAmountSum(BigDecimal miningAmountSum) {
        this.miningAmountSum = miningAmountSum;
    }

    public BigDecimal getConsumedAmountSum() {
        return consumedAmountSum;
    }

    public void setConsumedAmountSum(BigDecimal consumedAmountSum) {
        this.consumedAmountSum = consumedAmountSum;
    }

    public Date getLastSumTime() {
        return lastSumTime;
    }

    public void setLastSumTime(Date lastSumTime) {
        this.lastSumTime = lastSumTime;
    }

    public Date getLastMiningTime() {
        return lastMiningTime;
    }

    public void setLastMiningTime(Date lastMiningTime) {
        this.lastMiningTime = lastMiningTime;
    }

    @Override
    public String toString() {
        return "RebateUserMiningConsumeRecord{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", miningAmountSum=" + miningAmountSum +
                ", consumedAmountSum=" + consumedAmountSum +
                ", lastSumTime=" + lastSumTime +
                ", lastMiningTime=" + lastMiningTime +
                '}';
    }
}