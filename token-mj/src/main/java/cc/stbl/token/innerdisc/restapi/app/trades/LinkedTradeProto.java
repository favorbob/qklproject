package cc.stbl.token.innerdisc.restapi.app.trades;

import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class LinkedTradeProto {
    @Data
    @ApiModel("交易汇总查询")
    public static class RequestSummary{
        @NotNull(message = "交易类型不能为空")
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;

    }
    @Data
    public static class ResponseSummary{
        @ApiModelProperty("历史最高价")
        private BigDecimal highPrice;
        @ApiModelProperty("历史最低价")
        private BigDecimal lowPrice;
        @ApiModelProperty("累计成交量(单位万)")
        private BigDecimal totalValue;

    }
    @Data
    @ApiModel("交易列表查询")
    public static class RequestTradeList extends BaseProto.RquestPager{
 /*       @ApiModelProperty("日期 yyyy-MM-dd")
        @NotNull(message = "不能为空")
        private Date date;
*/
        @ApiModelProperty("开始日期 yyyy-MM-dd")
        private Date startDate;
        @ApiModelProperty("结束日期 yyyy-MM-dd")
        private Date endDate;
        @ApiModelProperty("排序字段, 时间: createDate ,数量:tradedCount,价格:singlePrice")
        private String orderBy = "createDate";
        @ApiModelProperty("倒序: true/false")
        private Boolean desc = true;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("类型 1 购买、2 卖出")
        private Integer type;// : int, //类型 0 购买、1 卖出
//        @ApiModelProperty("挂单类型 1: 用户挂单,2:系统挂单")
//        private Integer systemLinked;
    }

    @Data
    public static class RequestUserTradeList extends BaseProto.RquestPager{
        @NotNull(message = "挂单类型不能为空")
        @ApiModelProperty(value = "挂单类型： 1：求购，2：卖出")
        private Integer type;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
    }
    @Data
    public static class ResponseMaketInfo {
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("交易量")
        private Long volume;
        @ApiModelProperty("今日均价")
        private BigDecimal todayAveragePrice;
        @ApiModelProperty("0点时价")
        private BigDecimal lastdayPrice;
    }
    @Data
    public static class ResponseTradeInfo {
        private Integer hasRecord;
        private String recordId;
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
        @ApiModelProperty("类型 1 购买、2 卖出")
        private Integer type;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("1处理中，2：挂单成功,3：撤销 ")
        private Integer status;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("自己的单 ")
        private boolean self;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("区块号码")
        private Long blockNumber;
        public Integer getShowCancel(){
            if(this.status == 2 || this.status == 1) return 1;
            return 0;
        }
        public String getStatusDescr(){
            return this.status == 1 || this.status == 2 ? "挂单成功" : this.status == 3 ? "挂单完成" : "挂单取消";
        }
    }

    @Data
    public static class ResponseUserTradeInfo extends ResponseTradeInfo{
        private Integer hasRecord;
        private String recordId;
        private Integer tradeAssetCount;
        @ApiModelProperty("提醒状态 0:未提醒，1：已支付提醒，2：提醒已处理")
        private Integer tipStatus;
        @ApiModelProperty("受理状态0:未处理,1:申请交易,2:拒绝交易")
        private Integer accepted;
        @ApiModelProperty("交易状态:1,交易申请中,2,交易申请成功,3,交易完成,4交易取消")
        private Integer tradeStatus;
        @ApiModelProperty("交易类型:2:AIIC,4:MJ")
        private Integer balanceType;
        @ApiModelProperty("区块号码")
        private Long blockNumber;
        @ApiModelProperty("交易hash")
        private  String transactionHash;
        public String getStatusDescr(){
            if(getStatus() != 2 ) return super.getStatusDescr();
            if(hasRecord == 0) return super.getStatusDescr();
            return getType() == TradeConsts.LINKED_TYPE_SELL ? "卖出中" : "买入中";
        }
        public Integer getShowPay(){
            if(hasRecord == 0) return 0;
            if(getType() == TradeConsts.LINKED_TYPE_SELL) return 0;
            if(this.tradeStatus == 2) return 1;
            return 0;
        }

        public Integer getShowConfirm(){
            if(hasRecord == 0) return 0;
            if(getType() == TradeConsts.LINKED_TYPE_BUY) return 0;
            if(this.tradeStatus == 2 && this.tipStatus == TradeConsts.LINKED_RECORD_TIP_STATUS_IS_TIP) return 1;
            return 0;
        }

        public Integer getShowCancel(){
            if(hasRecord == 1) return 0;
            if(getShowCancelRecord() == 1) return 0;
            return super.getShowCancel();
        }

        public Integer getShowCancelRecord(){
            if(hasRecord == 0) return 0;
            if(getType() == TradeConsts.LINKED_TYPE_SELL && this.tipStatus == TradeConsts.LINKED_RECORD_TIP_STATUS_IS_TIP) return 0;
            if(this.tradeStatus == 2 || this.tradeStatus == 1) return 1;
            return 0;
        }

        public Integer getShowTips(){
            if(hasRecord == 0) return 0;
            if(getType() == TradeConsts.LINKED_TYPE_SELL) return 0;
//            return this.tipStatus == 0 || this.tipStatus == 2 ? 1 : 0;
            return 1;
        }
    }

    @Data
    public static class RequestApplyLinkedSeller {
        @NotNull(message = "不能为空")
//        @Size(message = "{min}和{max}之间")
        @ApiModelProperty("卖出单价")
        private BigDecimal sellAmount;
        @ApiModelProperty("卖出数量")
//        @Min(value = 100,message = "挂单资产少于100")
        @NotNull
        private Integer sellCount;
        @ApiModelProperty("交易类型 2:AIIC 4:MJ")
        @NotNull(message = "balanceType不能为空")
        private Integer balanceType;
    }

    @Data
    public static class RequestApplyLinkedBuy {
        @ApiModelProperty("买入单价")
        @NotNull
        private BigDecimal buyAmount;
        @ApiModelProperty("买入数量")
        @NotNull
        private Integer buyCount;
/*        @ApiModelProperty("支付方式")
        @NotNull
        private String payMethod;*/
        @ApiModelProperty("交易类型 2:AIIC 4:MJ")
        @NotNull(message = "balanceType不能为空")
        private Integer balanceType;
    }

    @Data
    public static class RequestBuyAsset {
        @ApiModelProperty("挂单记录id")
        @NotNull
        private String linkedId;
        @ApiModelProperty("买入数量")
//        @NotNull
//        @Min(value = 100,message = "挂单资产少于100")
        private Integer buyCount;
        @ApiModelProperty("交易类型 2:AIIC 4:MJ")
        @NotNull(message = "balanceType不能为空")
        private Integer balanceType;
    }

    @Data
    public static class ResponseBuyAsset {
        @ApiModelProperty("交易记录Id")
        private String recordId;
    }

    @Data
    public static class RequestSellAsset {
        @ApiModelProperty("挂单记录id")
        @NotNull
        private String linkedId;
        @ApiModelProperty("卖出数量")
        @NotNull
        private Integer buyCount;
        @ApiModelProperty("支付密码")
        @NotNull
        private String payPassword;
    }

    @Data
    public static class ResponseSellAssetPageData {
        @ApiModelProperty("卖家用户id")
        private String sellerUserId;
        @ApiModelProperty("收款码图片列表")
        private List<VrUserImgProto> codeList;
        // 用户信息
        @ApiModelProperty("我的资产")
        private BigDecimal myAsset;
        @ApiModelProperty("我的用户id")
        private String userId;
        // 挂单信息
        @ApiModelProperty("挂单记录id")
        private String recordId;
        @ApiModelProperty("卖出资产")
        private BigDecimal soldAsset;
        @ApiModelProperty("卖出单价")
        private BigDecimal price;// price : amount, //销售单价
        @ApiModelProperty("卖出金额")
        private BigDecimal amount;
        @ApiModelProperty("报单时间 格式 2018-06-15 15:52")
        private Date date;// : string, //报单时间 格式 2018-06-15 15:52
        @ApiModelProperty("类型 0 购买、1 卖出")
        private Integer type;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("0 已撤销、1 完成、2 挂单中")
        private Integer status;// : int, //类型 0 购买、1 卖出
    }

    @Data
    public static class ResponseBuyAssetPageData {
        @ApiModelProperty("卖家用户id")
        private String sellerUserId;
        @ApiModelProperty("收款码图片列表")
        private List<VrUserImgProto> codeList;
        // 用户信息
        @ApiModelProperty("我的用户id")
        private String userId;
        // 挂单信息
        @ApiModelProperty("挂单记录id")
        private String recordId;
        @ApiModelProperty("已收资产")
        private BigDecimal soldAsset;
        @ApiModelProperty("资产单价")
        private BigDecimal price;// price : amount, //销售单价
        @ApiModelProperty("总计金额")
        private BigDecimal amount;
        @ApiModelProperty("报单时间 格式 2018-06-15 15:52")
        private Date date;// : string, //报单时间 格式 2018-06-15 15:52
        @ApiModelProperty("类型 0 购买、1 卖出")
        private Integer type;// : int, //类型 0 购买、1 卖出
        @ApiModelProperty("0 已撤销、1 完成、2 挂单中")
        private Integer status;// : int, //类型 0 购买、1 卖出
    }
    @Data
    public static class VrUserImgProto {

        private String id;

        @ApiModelProperty("用户id")
        private String userId;

        @ApiModelProperty("图片url")
        private String imgUrl;

        @ApiModelProperty("类型：1微信码/2支付宝码")
        private Integer imgType;

        @ApiModelProperty("状态：0闲置/1使用中")
        private Integer status;

        private Date createDate;

        @ApiModelProperty("是否默认：0否/1是")
        private Integer isDefault;
    }

    @Data
    public static class RequestConfirmTrade {
        @NotNull
        private String payPassword;
        @NotNull
        private String recordId;
    }

    @Data
    public static class RequestCancelLinked {
        @ApiModelProperty("挂单记录id")
        @NotNull
        private String linkedId;
    }

    @Data
    public static class RequestBuySysAsset {
        private Integer buyCount;
    }

    @Data
    public static class ResponseBuySysAsset {
        private String recordId;
    }


}
