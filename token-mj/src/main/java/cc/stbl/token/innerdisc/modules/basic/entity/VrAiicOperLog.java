package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/09/28 17:36:40
*/
public class VrAiicOperLog {
    /**
    * 字段：vr_aiic_oper_log.id；备注：
     */
    private String id;

    /**
    * 字段：vr_aiic_oper_log.oper_date；备注：操作时间
     */
    private Date operDate;

    /**
    * 字段：vr_aiic_oper_log.user_id；备注：管理员id，手动修改时有值
     */
    private String userId;

    /**
    * 字段：vr_aiic_oper_log.before_aiic；备注：修改前aiic价格
     */
    private BigDecimal beforeAiic;

    /**
    * 字段：vr_aiic_oper_log.after_aiic；备注：修改后aiic价格
     */
    private BigDecimal afterAiic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBeforeAiic() {
        return beforeAiic;
    }

    public void setBeforeAiic(BigDecimal beforeAiic) {
        this.beforeAiic = beforeAiic;
    }

    public BigDecimal getAfterAiic() {
        return afterAiic;
    }

    public void setAfterAiic(BigDecimal afterAiic) {
        this.afterAiic = afterAiic;
    }
}