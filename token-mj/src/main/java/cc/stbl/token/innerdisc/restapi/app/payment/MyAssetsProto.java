package cc.stbl.token.innerdisc.restapi.app.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

public class MyAssetsProto {

    @Data
    public static class RequestType{
        private Integer type;//0:转入,1:转出
    }

    @Data
    public static class ResponseMyAssetsSupports{
        @ApiModelProperty("限制性资产")
        private String restrictedAssets; //限制性资产
        @ApiModelProperty("可用资产")
        private String availableAssets; //可用资产
        @ApiModelProperty("冻结资产")
        private String translateAssets; //冻结资产
        @ApiModelProperty("释放比例")
        private BigDecimal releaseRatio; //释放比例

        @ApiModelProperty("总资产(避免算有变设备需要重新发版)")
        private String totalAssets; //限制性资产+冻结资产
        @ApiModelProperty("积分")
        private String integral;
        @ApiModelProperty("钱包地址")
        private String walletAddress;
        
        @ApiModelProperty("A区")
        private Long aArea;
        @ApiModelProperty("b区")
        private Long bArea;
    }

    @Data
    public static class ResponseTradeDetail{
        @ApiModelProperty("交易号")
        private String tradeNo;
        @ApiModelProperty("卖出资产")
        private BigDecimal tradeAmount;
        @ApiModelProperty("买入账号")
        private String toUser;
        @ApiModelProperty("交易时间")
        private Date createDate;
        @ApiModelProperty("支付方式")
        private String payment="线下支付";
        @ApiModelProperty("收益金额")
        private String profitAmount;
        private Long blockNumber;
        private String transactionHash;
    }

    @Data
    public static class ResponseTradeInfo{
        private String id;
        @ApiModelProperty("金额")
        private BigDecimal tradeAmount;
        @ApiModelProperty("交易号")
        private String tradeNo;
        @ApiModelProperty("收入/支出")
        private String balance;
        @ApiModelProperty("创建时间")
        private Date date;
        @ApiModelProperty("备注")
        private String remark;
        private Long blockNumber;
        private String transactionHash;
    }

    @Data
    public static class ResponseAssetInfo{
        private String id;
        @ApiModelProperty("交易时间")
        private Date createTime;
        @ApiModelProperty("备注")
        private String remark;
        @ApiModelProperty("金额")
        private BigDecimal tradeAmount;
        @ApiModelProperty("收入/支出")
        private String balance;
        private Long blockNumber;
        private String transactionHash;
    }

}
