package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@ApiModel
public class EthTradeRecordProto {
    @Data
    public static class RequestListCondition extends BaseProto.RquestPager {
        @ApiModelProperty("用户id")
        private String uid;
        @ApiModelProperty("支付方式")
        private String payMethod;
        @ApiModelProperty("账号")
        private String phoneNum;
        @ApiModelProperty("开始时间")
        private Date startTradeDate;
        @ApiModelProperty("结束时间")
        private Date entTradeDate;
        @ApiModelProperty("交易号")
        private String tradeNo;
    }

    @Data
    public static class RequestDetail{
        @NotBlank
        @ApiModelProperty("id")
        private String id;
    }

    @Data
    public static class ResponseGetTradeRecords {
        private String id;
        @ApiModelProperty("交易号")
        private String tradeNo;

        private String fromUserId;

        private String toUserId;

        @ApiModelProperty("资金数量")
        private Integer asset;
        @ApiModelProperty("资金单价")
        private Long price;
        @ApiModelProperty("资金金额")
        private Integer amount;

        private Date createDate;
        @ApiModelProperty("交易成功时间")
        private Date successDate;
        @ApiModelProperty("1.交易申请中，2：交易成功")
        private Integer tradeStatus;
        @ApiModelProperty("交易类型")
        private Integer tradeType;

        @ApiModelProperty("用户等级,1:系统，2:团队领导,9:普通用户")
        private Integer userLevel;
        @ApiModelProperty("受限资产")
        private BigInteger restrictedAssets;
        @ApiModelProperty("可用资产")
        private BigInteger availableAssets;
        @ApiModelProperty("总积分")
        private BigInteger integral;
        @ApiModelProperty("用户账号")
        private String account;
        @ApiModelProperty("用户手机号")
        private String phoneNum;
    }

    /**
     * 买入卖出用户信息
     */
    @Data
    public static class ResponseDetail {
        @ApiModelProperty("交易记录ID")
        private String id;
        @ApiModelProperty("交易号")
        private String tradeNo;
        @ApiModelProperty("交易时间")
        private Date successDate;
        @ApiModelProperty("1.交易申请中，2：交易成功")
        private Integer tradeStatus;
        @ApiModelProperty("交易类型")
        private Integer tradeType;
        @ApiModelProperty("支付方式")
        private BigInteger payMethod;

        @ApiModelProperty("用户ID")
        private String userId;
        @ApiModelProperty("用户账号")
        private String account;
        @ApiModelProperty("用户手机号")
        private String phoneNum;

        @ApiModelProperty("受限资产")
        private BigInteger restrictedAssets;
        @ApiModelProperty("可用资产")
        private BigInteger availableAssets;
        @ApiModelProperty("总积分")
        private BigInteger integral;

        @ApiModelProperty("资金数量 兑换资金数量")
        private BigDecimal asset;
        @ApiModelProperty("资金单价")
        private Long price;
        @ApiModelProperty("资金金额")
        private Integer amount;
        @ApiModelProperty("获得积分数量")
        private BigDecimal acquired_integral;
        @ApiModelProperty("兑换账户 转入资金账户 1:受限,2:可用")
        private Integer flowType;

    }
}
