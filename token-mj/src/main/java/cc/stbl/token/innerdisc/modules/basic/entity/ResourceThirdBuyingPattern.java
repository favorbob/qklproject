package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;

/**
* create by framework, create date 2018/08/22 14:23:11
*/
public class ResourceThirdBuyingPattern {
    /**
    * 字段：resource_third_buying_pattern.id；备注：
     */
    private String id;

    /**
    * 字段：resource_third_buying_pattern.buying_pattern；备注：1：零钱，2：资产
     */
    private Integer buyingPattern;

    /**
    * 字段：resource_third_buying_pattern.buying_category；备注：1：解锁前(游戏)，2：解锁后(游戏)，0：默认(电影用)
     */
    private Integer buyingCategory;

    /**
    * 字段：resource_third_buying_pattern.priority；备注：
     */
    private Integer priority;

    /**
    * 字段：resource_third_buying_pattern.price；备注：
     */
    private BigDecimal price;

    /**
     * 字段：resource_third_buying_pattern.settings_id；备注：
     */
    private String settingsId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBuyingPattern() {
        return buyingPattern;
    }

    public void setBuyingPattern(Integer buyingPattern) {
        this.buyingPattern = buyingPattern;
    }

    public Integer getBuyingCategory() {
        return buyingCategory;
    }

    public void setBuyingCategory(Integer buyingCategory) {
        this.buyingCategory = buyingCategory;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(String settingsId) {
        this.settingsId = settingsId;
    }

    @Override
    public String toString() {
        return "ResourceThirdBuyingPattern{" +
                "id='" + id + '\'' +
                ", buyingPattern=" + buyingPattern +
                ", buyingCategory=" + buyingCategory +
                ", priority=" + priority +
                ", price=" + price +
                ", settingsId='" + settingsId + '\'' +
                '}';
    }
}