package cc.stbl.token.innerdisc.restapi.app.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

public class CommonProto {

    @Data
    public static class ResponseParameters {
        @ApiModelProperty("aiic上浮比例")
        private BigDecimal aiicRiseRatio;
        @ApiModelProperty("aiic下浮比例")
        private BigDecimal aiicFallRatio;
        @ApiModelProperty("mj上浮比例")
        private BigDecimal mjRiseRatio;
        @ApiModelProperty("mj下浮比例")
        private BigDecimal mjFallRatio;
        @ApiModelProperty("aiic卖出最小数量")
        private Long aiicSellMinNum;
        @ApiModelProperty("aiic卖出最大数量")
        private Long aiicSellMaxNum;
        @ApiModelProperty("aiic买入最小数量")
        private Long aiicBuyMinNum;
        @ApiModelProperty("aiic买入最大数量")
        private Long aiicBuyMaxNum;
        @ApiModelProperty("mj卖出最小数量")
        private Long mjSellMinNum;
        @ApiModelProperty("mj卖出最大数量")
        private Long mjSellMaxNum;
        @ApiModelProperty("mj买入最小数量")
        private Long mjBuyMinNum;
        @ApiModelProperty("mj买入最大数量")
        private Long mjBuyMaxNum;

    }
}
