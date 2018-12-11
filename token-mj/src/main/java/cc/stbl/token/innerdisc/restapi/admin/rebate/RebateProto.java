package cc.stbl.token.innerdisc.restapi.admin.rebate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel
public class RebateProto {

    @Data
    public static class RebateConfig {
        @ApiModelProperty(value = "修改需要传id")
        private String id;
        @NotNull(message = "请输入上下线总资产比例")
        @ApiModelProperty(value = "上下线总资产比例")
        private Float ratio;
        @NotNull(message = "请输入加速倍数")
        @ApiModelProperty(value = "加速倍数")
        private Float rebateRatio;
    }

    @Data
    public static class InvolSetting {

        @ApiModelProperty(value = "资产加速设置列表")
        private List<RebateConfig> rebateConfigList;

        @ApiModelProperty(value = "系统上线")
        private Boolean systemOnline;

        @ApiModelProperty(value = "定时上线时间")
        private Date systemOnLineTime;

        @ApiModelProperty(value = "挂单平台开关")
        private Boolean linkedOnOff;

        @ApiModelProperty(value = "资产与积分比例: 资产")
        private BigDecimal asset;

        @ApiModelProperty(value = "资产与积分比例: 积分")
        private BigDecimal integral;

        @ApiModelProperty(value = "初始值比例设置")
        private BigDecimal initIntegralRebateRatio;

        @ApiModelProperty(value = "积分转化资产设置:可挂单资产占比")
        private BigDecimal availableAssets;

        @ApiModelProperty(value = "积分转化资产设置:受限资产占比")
        private BigDecimal limitedAssets;


    }


}
