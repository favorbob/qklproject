package cc.stbl.token.innerdisc.restapi.admin.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class VrUserAssetProto {

    @Data
    public static class ResponseAsstFlow {
        private String id;
        @ApiModelProperty("交易号")
        private String tradeNo;

        private String userId;
        @ApiModelProperty("交易金额")
        private BigDecimal tradeAmount;
        @ApiModelProperty("剩余金额")
        private BigDecimal remainAmount;
        @ApiModelProperty("流水类型")
        private Integer tradeType;

        private String tradeTypeName;
        @ApiModelProperty("业务类型")
        private Integer businessType;
        @ApiModelProperty("加/减")
        private Boolean isPlus;
        @ApiModelProperty("创建时间")
        private Date createTime;
        @ApiModelProperty("交易hash")
        private String transactionHash;
        @ApiModelProperty("区块高度")
        private Long atBlockNumber;
    }

    @Data
    public static class ResponseIntegralFlow {

        private String id;

        @ApiModelProperty("交易号")
        private String tradeNo;

        @ApiModelProperty("用户id")
        private String userId;

        @ApiModelProperty("积分金额")
        private BigDecimal tradeIntegral;

        @ApiModelProperty("剩余积分")
        private BigDecimal remainIntegral;

        @ApiModelProperty("交易类型，0收入，1支出")
        private Integer tradeType;

        @ApiModelProperty("业务类型")
        private Integer businessType;

        @ApiModelProperty("创建时间")
        private Date createTime;

        @ApiModelProperty("交易hash")
        private String transactionHash;

        @ApiModelProperty("区块高度")
        private Long atBlockNumber;
    }

}
