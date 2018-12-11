package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/20 18:00:12
*/
public class RebateUserDailyIncomeStatistics {
    /**
    * 字段：rebate_user_daily_income_statistics.id；备注：
     */
    private String id;

    /**
    * 字段：rebate_user_daily_income_statistics.user_id；备注：
     */
    private String userId;

    /**
    * 字段：rebate_user_daily_income_statistics.statistics_date；备注：统计日期
     */
    private Date statisticsDate;

    /**
    * 字段：rebate_user_daily_income_statistics.income；备注：当日收益
     */
    private BigDecimal income;

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

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "RebateUserDailyIncomeStatistics{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", statisticsDate=" + statisticsDate +
                ", income=" + income +
                '}';
    }
}