package cc.stbl.token.innerdisc.modules.trades.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
* create by framework, create date 2018/08/22 17:48:05
*/
public class TwdLinkedTradeStatDay {
    /**
    * 字段：twd_linked_trade_stat_day.day；备注：统计日期 yyyy-MM-dd
     */
    private Date day;

    /**
    * 字段：twd_linked_trade_stat_day.today_trade_asset；备注：今日交易总额
     */
    private BigDecimal todayTradeAsset;

    /**
    * 字段：twd_linked_trade_stat_day.yesterday_trade_asset；备注：昨日交易总额
     */
    private BigDecimal yesterdayTradeAsset;

    /**
    * 字段：twd_linked_trade_stat_day.total_trade_asset；备注：总交易额
     */
    private BigDecimal totalTradeAsset;

    /**
    * 字段：twd_linked_trade_stat_day.eleven_trade_asset；备注：7日前交易资产
     */
    private BigDecimal elevenTradeAsset;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public BigDecimal getTodayTradeAsset() {
        return todayTradeAsset;
    }

    public void setTodayTradeAsset(BigDecimal todayTradeAsset) {
        this.todayTradeAsset = todayTradeAsset;
    }

    public BigDecimal getYesterdayTradeAsset() {
        return yesterdayTradeAsset;
    }

    public void setYesterdayTradeAsset(BigDecimal yesterdayTradeAsset) {
        this.yesterdayTradeAsset = yesterdayTradeAsset;
    }

    public BigDecimal getTotalTradeAsset() {
        return totalTradeAsset;
    }

    public void setTotalTradeAsset(BigDecimal totalTradeAsset) {
        this.totalTradeAsset = totalTradeAsset;
    }

    public BigDecimal getElevenTradeAsset() {
        return elevenTradeAsset;
    }

    public void setElevenTradeAsset(BigDecimal elevenTradeAsset) {
        this.elevenTradeAsset = elevenTradeAsset;
    }

    @Override
    public String toString() {
        return "TwdLinkedTradeStatDay{" +
                "day=" + day +
                ", todayTradeAsset=" + todayTradeAsset +
                ", yesterdayTradeAsset=" + yesterdayTradeAsset +
                ", totalTradeAsset=" + totalTradeAsset +
                ", elevenTradeAsset=" + elevenTradeAsset +
                '}';
    }
}