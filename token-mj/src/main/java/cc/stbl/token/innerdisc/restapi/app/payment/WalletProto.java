package cc.stbl.token.innerdisc.restapi.app.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stbl.payment.providerImpl.waller.bean.WalletAccontItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WalletProto {

    @Data
    public static class ResponseWalletSupports{
        @ApiModelProperty("用户ID")
        private String uid;
        @ApiModelProperty("创建时间")
        private String createTime;		// 创建时间
        @ApiModelProperty("钱包ID")
        private String walletId;
        @ApiModelProperty("钱包余额")
        private Long money;//钱包余额
        private List<WalletAccontItem> accountItems;
    }

    @Data
    public static class RechargeRequestSupports{
        @NotBlank(message = "支付方法")
        private String payMethod;
        @NotBlank(message = "金额")
        private BigDecimal amount;
    }

    @Data
    public static class ReqBindSupports{
        @NotBlank(message = "账号")
        private String account;
        @NotBlank(message = "绑定方法")
        private String bindMethod;
        @NotBlank(message = "姓名")
        private String userName;
        @NotBlank(message = "电话")
        private String phone;
    }

    @Data
    public static class ReqWalletListSupports{
        private String userId;
        private Integer pageSize;
        private Integer pageNo;
        private String partner;
        private String direction = "down";
        @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
        @NotBlank(message = "开始时间")
        private Date startTradeTime;
        @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
        @NotBlank(message = "结束时间")
        private Date endTradeTime;
        private Integer limit = Integer.valueOf(20);
        private String flag;
    }

    @Data
    public static class ResponseWaterFlowSupports{
        @ApiModelProperty("流水ID")
        private String id;
        @ApiModelProperty("商户号")
        private String partner;
        @ApiModelProperty("用户ID")
        private String userId;
        @ApiModelProperty("支付订单号")
        private String payOrderNo;
        @ApiModelProperty("交易流水号")
        private String tradeOrderNo;
        @ApiModelProperty("业务订单Id")
        private String businessId;
        @ApiModelProperty("交易流水号")
        private String businessType;
        @ApiModelProperty("支付流水号")
        private String payWaterNo;
        private String payId;
        private Long tradeAmount;
        @ApiModelProperty("交易时间")
        @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
        private Date tradeDate;
        @ApiModelProperty("交易类型:0:收入，1，支出，2:内部账号动账")
        private Integer tradeType;
        private String remark;
        @ApiModelProperty("交易种类:1:充值,2:消费,3:提现,4:退款,5:收入,6:冻结,7:解冻,8:企业付款API,9:钱包转账")
        private Integer tradeCategory;
    }

    @Data
    public static class RequestCreateWallet {
        @NotNull
        private String payPassword;
        @NotNull
        private String walletName;
    }
}
