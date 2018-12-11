package cc.stbl.token.innerdisc.restapi.admin.trades;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class VrTokenTradeProto {

    @Data
    public static class DeductAssetBalanceRequest {
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        @NotNull(message = "支付金额不能为空")
        private BigDecimal amount;
    }

    @Data
    public static class ChargeAssetBalanceRequest {
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        @NotNull(message = "支付金额不能为空")
        private BigDecimal amount;
    }

    @Data
    public static class ChargeSubAssetBalanceRequest {
        @NotBlank(message = "下级用户ID不能为空")
        private String subUserId;
        @NotNull(message = "支付金额不能为空")
        private BigDecimal amount;
        @NotNull(message = "支付密码不能为空")
        private String payPwd;
        @ApiModelProperty("备注")
        private String remark;

    }

    @Data
    public static class IntegralAmplifyAllRequest {
        @NotBlank(message = "用户ID不能为空")
        private String userId;
    }

    @Data
    public static class AssetFlowWaterRequest {
        private Integer pageNo = 1;
        private Integer pageSize = 20;
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        @ApiModelProperty("资产类型: public static final int FLOW_TYPE_LOCKED_BALANCE = 1; //冻结\n" +
                "    public static final int FLOW_TYPE_BALANCE = 2; //可用\n" +
                "    public static final int FLOW_TYPE_INTEGRAL = 3; //积分\n" +
                "    public static final int FLOW_TYPE_LIMITED_BALANCE = 4; //受限")
        private Integer tradeType;
    }
}
