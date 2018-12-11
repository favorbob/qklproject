package cc.stbl.token.innerdisc.restapi.app.trades;

import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class LinkedTradeRecordProto {

    @Data
    public static class RequestUserRecord extends BaseProto.RquestPager{
        @ApiModelProperty("类型 1 购买、2 卖出")
        @NotNull
        private Integer type;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("挂单id,非必传 ")
        private String linkedId;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
    }

    @Data
    public static class ResponseUserRecord {
        private Date date;// : string, //时间 格式 2018-06-15 15:52
        private String name;// : string, //用户昵称
        private String userId; //用户id
        private Integer linkedType;
        @ApiModelProperty("资产")
        private Integer Amount;
        private String linkedId;
        private String recordId;
        @ApiModelProperty("销售单价")
        private String singlePrice;
        @ApiModelProperty("记录描述")
        private String content;// : string, //记录描述 如：购买20资产花费321.00元
        @ApiModelProperty("提醒状态 0:未提醒，1：已支付提醒，2：提醒已处理")
        private Integer tipStatus;
        /**
         * 字段：twd_linked_trade_record.accepted；备注：受理状态0:未处理,1:申请交易,2:拒绝交易
         */
        @ApiModelProperty("受理状态0:未处理,1:申请交易,2:拒绝交易")
        private Integer accepted;
        @ApiModelProperty("交易状态:1,交易申请中,2,交易申请成功,3,交易完成,4交易取消")
        private Integer tradeStatus;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("区块号码")
        private Integer blockNumber;
        @ApiModelProperty("交易hash")
        private  String transactionHash;
        @ApiModelProperty("卖家电话")
        private  String sellerPhoneNum;
        public String getStatusDescr(){
            return this.tradeStatus == 1 || this.tradeStatus == 2 ? "交易中" : this.tradeStatus == 3 ? "交易完成" : "交易取消";
        }
        public Integer getShowCancelTrade(){
            //挂单卖
            if(linkedType == TradeConsts.LINKED_TYPE_SELL && getShowTips() == 1) return 0;
            if(this.tradeStatus == 2 || this.tradeStatus == 1) return 1;
            return 0;
        }
        public Integer getShowPay(){
            if(this.tradeStatus == 2 && linkedType == TradeConsts.LINKED_TYPE_BUY) return 1;
            return 0;
        }
        public Integer getShowTips(){
            return 1;
//            return this.tipStatus == 0 || this.tipStatus == 2 ? 1 : 0;
        }
    }

    @Data
    public static class RequestTipPayed {
        @NotNull
        private String recordId;
//        private String extendId;
    }
    @Data
    public static class RequestToUserRecord extends BaseProto.RquestPager{
        @ApiModelProperty("类型 1 购买、2 卖出")
        @NotNull
        private Integer type;// : int, //类型 1 购买、2 卖出
    }

    @Data
    public static class ResponseToUserRecord {
        private Date date;// : string, //时间 格式 2018-06-15 15:52
        private String name;// : string, //用户昵称
        private String userId; //用户id
        private Integer linkedType;
        private Integer Amount;
        private String linkedId;
        private String recordId;
        private String singlePrice;
        private String content;// : string, //记录描述 如：购买20资产花费321.00元
        /**
         * 字段：twd_linked_trade_record.accepted；备注：受理状态0:未处理,1:申请交易,2:拒绝交易
         */
        @ApiModelProperty("受理状态0:未处理,1:申请交易,2:拒绝交易")
        private Integer accepted;
        @ApiModelProperty("提醒状态 0:未提醒，1：已支付提醒，2：提醒已处理")
        private Integer tipStatus;
        @ApiModelProperty("交易状态:1,交易申请中,2,交易申请成功,3,交易完成,4交易取消")
        private Integer tradeStatus;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("区块号码")
        private Integer blockNumber;

        public String getStatusDescr(){
            return this.tradeStatus == 1 || this.tradeStatus == 2 ? "交易中" : this.tradeStatus == 3 ? "交易完成" : "交易取消";
        }

        public Integer getShowConfirm(){
            if(linkedType == TradeConsts.LINKED_TYPE_SELL) return 0;
            if(this.tradeStatus == 2 && this.tipStatus == TradeConsts.LINKED_RECORD_TIP_STATUS_IS_TIP) return 1;
            return 0;
        }

        public Integer getShowCancelTrade(){
            //向别人挂的单卖
            if(linkedType == TradeConsts.LINKED_TYPE_BUY && this.tipStatus != 0) return 0;
            if(this.tradeStatus == 2 || this.tradeStatus == 1) return 1;
            return 0;
        }

        public Integer getShowPay(){
            if(linkedType == TradeConsts.LINKED_TYPE_BUY) return 0;
            if(this.tradeStatus == 2) return 1;
            return 0;
        }

        public Integer getShowTips(){
            if(linkedType == TradeConsts.LINKED_TYPE_BUY) return 0;
//            return this.tipStatus == 0 || this.tipStatus == 2 ? 1 : 0
            return 1;
        }
    }

    @Data
    public static class RequestRecordDetail {
        private String recordId;
    }

    @Data
    public static class ResponseRecordDetail {
        private String recordId;
        private String linkedId;
        private String fromUserId;
        private String toUserId;
        private Date successDate;
        private Integer linkedType;
        private Integer assetCount;
        private Long singleAmount;
        private Long totalAmount;
        private Integer tradeStatus;
        private Date createDate;
        private Integer accepted;
        private Integer systemLinked;
        private Integer tipStatus;
    }
}
