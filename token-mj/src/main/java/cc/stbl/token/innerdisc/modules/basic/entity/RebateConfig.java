package cc.stbl.token.innerdisc.modules.basic.entity;

/**
* create by framework, create date 2018/08/20 15:24:18
*/
public class RebateConfig {
    /**
    * 字段：rebate_config.id；备注：
     */
    private String id;

    /**
    * 字段：rebate_config.ratio；备注：上下线总资产比例
     */
    private Float ratio;

    /**
    * 字段：rebate_config.rebate_ratio；备注：加速值
     */
    private Float rebateRatio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Float getRebateRatio() {
        return rebateRatio;
    }

    public void setRebateRatio(Float rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    @Override
    public String toString() {
        return "RebateConfig{" +
                "id='" + id + '\'' +
                ", ratio=" + ratio +
                ", rebateRatio=" + rebateRatio +
                '}';
    }
}