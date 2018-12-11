package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class LinkedTradeProto {
    @Data
    public static class ResponseNavyUser {
        @ApiModelProperty("用户id")
        private String navyUserId;
        @ApiModelProperty("用户昵称")
        private String nickName;
        @ApiModelProperty("用户资产")
        private BigDecimal asset;
    }

    @Data
    public static class RequestSysLinkedSeller {
        @ApiModelProperty("用户id")
        @NotNull
        private String navyUserId;
        @ApiModelProperty("交易单价")
        @NotNull
        private BigDecimal tradeAmount;
        @ApiModelProperty("资产数量")
        @NotNull
        private Integer tradeCount;
    }

    @Data
    public static class ResponseTradeInfo {
        @ApiModelProperty("卖家账户")
        private String account;// : string, //卖家账户
        @ApiModelProperty("挂单ID")
        private String linkedId;
        @ApiModelProperty("用户id")
        private String userId;
        @ApiModelProperty("销售单价")
        private Double price;// price : amount, //销售单价
        @ApiModelProperty("资产")
        private BigDecimal amount; //资产
        @ApiModelProperty("报单时间 格式 2018-06-15 15:52")
        private Date date;// : string, //报单时间 格式 2018-06-15 15:52
        @ApiModelProperty("类型 0 购买、1 卖出")
        private Integer type;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("0 已撤销、1 完成、2 挂单中")
        private Integer status;// : int, //类型 0 购买、1 卖出

    }

    @Data
    @ApiModel("交易列表查询")
    public static class RequestTradeList extends BaseProto.RquestPager{
        @ApiModelProperty("日期 yyyy-MM-dd")
//        @NotNull(message = "不能为空")
        private Date date;
        @ApiModelProperty("排序：0正序/1反序")
        private String orderMethod;
    }

    @Data
    public static class RequestPrice {
        @ApiModelProperty("价格")
        @NotNull
        private BigDecimal price; //价格
    }

    @Data
    public static class RequestUserBuyAssetList extends BaseProto.RquestPager{
        private Integer status;
        private String phone;
        private Date startDate;
        private Date endDate;
    }
    @Data
    public static class ResponseUserBuyAssetList {
        private String userId;
        private String userAccount;
        @ApiModelProperty("交易单价")
        private String tradeAmount;
        @ApiModelProperty("资产数量")
        private Integer tradeCount;
        @ApiModelProperty
        private String totalAmount;
        private String recordId;
        private Date tradeDate;
        @ApiModelProperty("0:未付款,1:已收款,2已拒绝")
        private Integer status;
        @ApiModelProperty("0:未提醒，1：提醒已付款")
        private Integer tipStatus;
    }

    @Data
    public static class RequestOnState {
        @ApiModelProperty("开启状态")
        private Boolean onState;
    }

    @Data
    public static class RequestConfirmTrade {
        private String recordId;
    }
}
