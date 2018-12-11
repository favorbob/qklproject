package cc.stbl.token.innerdisc.modules.basic.entity;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/24 14:52:32
*/
public class VrRedPaperLog {
    /**
    * 字段：vr_red_paper_log.id；备注：
     */
    private String id;

    /**
    * 字段：vr_red_paper_log.user_id；备注：用户id
     */
    private String userId;

    /**
    * 字段：vr_red_paper_log.time；备注：领取时间
     */
    private Date time;

    /**
     * 字段：vr_red_paper_log.time；备注：金额
     */
    private BigDecimal amount;

    public VrRedPaperLog() {}

    public VrRedPaperLog(String userId) {
        this.id = JavaUUIDGenerator.get();
        this.userId = userId;
        this.time = new Date();
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}