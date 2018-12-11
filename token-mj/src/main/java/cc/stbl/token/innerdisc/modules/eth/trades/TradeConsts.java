package cc.stbl.token.innerdisc.modules.eth.trades;

public class TradeConsts {

    //操作类型
    public static final Integer OPT_TYPE_BY_USER = 1;
    public static final Integer OPT_TYPE_BY_SYSTEM = 2;
    public static final Integer OPT_TYPE_BY_SYSTEM_USER = 3;

    //货币类型
    public static final int FLOW_TYPE_LOCKED_BALANCE = 1; //冻结
    public static final int FLOW_TYPE_BALANCE = 2; // AIIC
    public static final int FLOW_TYPE_INTEGRAL = 3; // 资产
    public static final int FLOW_TYPE_LIMITED_BALANCE = 4; // MJ

    public static String getBalanceTypeName(int type){
        if(type == FLOW_TYPE_LOCKED_BALANCE) return "冻结";
        if(type == FLOW_TYPE_BALANCE) return "AIIC";
        if(type == FLOW_TYPE_LIMITED_BALANCE) return "MJ";
        if(type == FLOW_TYPE_INTEGRAL) return "资产";
        return "";
    }


    //交易的状态类型
    public static final Integer TRADE_STATUS_ERROR = -1; //交易失败
    public static final Integer TRADE_STATUS_PROCESSING = 1; //交易申请中
    public static final Integer TRADE_STATUS_SUCCESS = 2; //交易申请成功
    public static final Integer TRADE_STATUS_COMPLETE = 3; //交易完成
    public static final Integer TRADE_STATUS_CANCEL = 4; //交易取消

    //业务类型
    public static final Integer TRADE_TYPE_SYS_CHARGE = 1; //充值
    public static final Integer TRADE_TYPE_SYS_DEDUCT = 2; //扣除
    public static final Integer TRADE_TYPE_TRANSFER = 3; //转账
    public static final Integer TRADE_TYPE_LOCK = 4; //冻结
    public static final Integer TRADE_TYPE_UN_LOCK = 5; //解冻
    public static final Integer TRADE_TYPE_LINKED = 6; //挂单平台交易冻结
    public static final Integer TRADE_TYPE_AMPLIFY = 7;//资产放大积分
    public static final Integer TRADE_TYPE_TRANSFER_AMPLIFY = 8;//转账后资产放大积分
    public static final Integer TRADE_TYPE_REBATE = 9; //积分返币
    public static final Integer TRADE_TYPE_BALANCE_MINT = 10; //挖矿
    public static final Integer TRADE_TYPE_CONSUME = 11; //消费
    public static final Integer TRADE_TYPE_UN_LINKED = 12; //挂单平台交易解冻
    public static final Integer TRADE_TYPE_LINKED_SELL_TRANSFER = 13; //挂单卖出
    public static final Integer TRADE_TYPE_LINKED_BUY_TRANSFER = 14; //挂单买入
    public static final Integer TRADE_TYPE_AMPLIFY_SYS = 15;//系统积分放大
    public static final Integer TRADE_TYPE_LOCK_TRANSFER = 16;//冻结资产转账
    public static final Integer TRADE_TYPE_LINKED_CONFIRM_TRADE = 17; //挂单交易确认
    public static final Integer TRADE_TYPE_LINKED_CANCEL_TRADE = 18; //挂单取消交易
    public static final Integer TRADE_TYPE_LINKED_CANCEL_LINKED = 19;  //取消挂单
    public static final Integer TRADE_TYPE_LINKED_SYS_CHARGE = 20; //向系统购买
    public static final Integer TRADE_TYPE_AMPLIFY_ACTIVE_USER = 21; //向系统购买
    //收支类型
    public static final boolean TRADE_FLOW_BTYPE_INCOME = true; //boolean 收入, !TRADE_FLOW_BTYPE_INCOME  支出
    public static final Integer TRADE_FLOW_TYPE_EXPEND = 1; //支出
    public static final Integer TRADE_FLOW_TYPE_INCOME = 0; //收入

    //挂单平台，挂单类型
    public static final Integer LINKED_TYPE_SELL = 2;
    public static final Integer LINKED_TYPE_BUY = 1;
    public static final Integer LINKED_TYPE_SYSTEM = 3;

    //审批状态
    public static final Integer ACCEPTED_STATUS_APPLY = 0; //申请 中
    public static final Integer ACCEPTED_STATUS_ALLOW = 1; //同意
    public static final Integer ACCEPTED_STATUS_REJECT = 2; //拒绝


    public static final Integer LINKED_RECORD_TIP_STATUS_IS_TIP = 1;
    public static final Integer LINKED_RECORD_TIP_STATUS_IS_READ = 2;
}
