package cc.stbl.token.innerdisc.common.enums;

import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;

public enum BEnum {

    SYS_CHARGE(TradeConsts.TRADE_TYPE_SYS_CHARGE,"eth_trade_record","系统充值"),//充值
    DEDUCT(TradeConsts.TRADE_TYPE_SYS_DEDUCT,"eth_trade_record","扣除"),//扣除
    TRANSFER(TradeConsts.TRADE_TYPE_TRANSFER,"eth_trade_record","转账"),//转账
    LOCK_TRANSFER(TradeConsts.TRADE_TYPE_LOCK_TRANSFER,"eth_trade_record","转账"),//转账
    LOCK(TradeConsts.TRADE_TYPE_LOCK,"eth_trade_record","冻结"),//冻结
    UN_LOCK(TradeConsts.TRADE_TYPE_UN_LOCK,"eth_trade_record","解冻"),//解冻
    LINKED_LOCK(TradeConsts.TRADE_TYPE_LINKED,"twd_linked_trade","挂单申请"),//挂单平台交易
    LINKED_UN_LOCK(TradeConsts.TRADE_TYPE_UN_LINKED,"twd_linked_trade","挂单撤销"),//挂单平台交易
    LINKED_SELL_TRANSFER(TradeConsts.TRADE_TYPE_LINKED_SELL_TRANSFER,"twd_linked_trade_record","挂单卖出"),//挂单平台交易
    LINKED_BUY_TRANSFER(TradeConsts.TRADE_TYPE_LINKED_BUY_TRANSFER,"twd_linked_trade_record","挂单买入"),//挂单平台交易
    LINKED_SYS_CHARGE(TradeConsts.TRADE_TYPE_LINKED_SYS_CHARGE,"twd_linked_trade_record","系统充值"),//挂单平台交易
    LINKED_CONFIRM_TRADE(TradeConsts.TRADE_TYPE_LINKED_CONFIRM_TRADE,"twd_linked_trade_record" ,"挂单确认"),
    LINKED_CANCEL_TRADE(TradeConsts.TRADE_TYPE_LINKED_CANCEL_TRADE,"twd_linked_trade_record" ,"挂单交易取消"),
    LINKED_CANCEL_LINKED(TradeConsts.TRADE_TYPE_LINKED_CANCEL_LINKED,"twd_linked_trade" ,"取消挂单"),
    AMPLIFY(TradeConsts.TRADE_TYPE_AMPLIFY, "eth_integral_amplify","积分兑换"), //资产放大积分
    AMPLIFY_SYS(TradeConsts.TRADE_TYPE_AMPLIFY_SYS, "eth_integral_amplify","积分兑换"), //资产放大积分
    ACTIVE_USER(TradeConsts.TRADE_TYPE_AMPLIFY_ACTIVE_USER, "eth_integral_amplify","激活用户"), //资产放大积分
    TRANSFER_AMPLIFY(TradeConsts.TRADE_TYPE_TRANSFER_AMPLIFY, "eth_trade_record","资产转入"),//转账后资产放大积分
    INTEGRAL_REBATE(TradeConsts.TRADE_TYPE_REBATE,"eth_returned_integral","积分返利"),//积分返币
    BALANCE_MINT(TradeConsts.TRADE_TYPE_BALANCE_MINT,"eth_mint_record","挖矿"),//挖矿
    CONSUME(TradeConsts.TRADE_TYPE_CONSUME,"eth_trade_record","消费"),//消费
    UNKNOWN(-1,"un_known","未知");

    public final Integer type;
    public final String tbName;
    public final String remark;

    BEnum(Integer type, String tbName, String remark) {
        this.type = type;
        this.tbName = tbName;
        this.remark = remark;
    }

    public static BEnum valueOfBType(Integer bType){
        for (BEnum bEnum : BEnum.values()) {
            if(bType == bEnum.type) return bEnum;
        }
        return UNKNOWN;
    }

    public static String getNameByCode(Integer bType) {
        return valueOfBType(bType).remark;
    }
}
