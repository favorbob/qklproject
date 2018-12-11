package cc.stbl.token.innerdisc.modules.basic.entity;

/**
* create by framework, create date 2018/08/20 18:00:12
*/
public class RebateUserRebateConfig {
    /**
    * 字段：rebate_user_config.user_id；备注：
     */
    private String userId;

    /**
    * 字段：rebate_user_config.curr_rebate_ratio；备注：当前加速比
     */
    private Float currRebateRatio;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getCurrRebateRatio() {
        return currRebateRatio;
    }

    public void setCurrRebateRatio(Float currRebateRatio) {
        this.currRebateRatio = currRebateRatio;
    }
}