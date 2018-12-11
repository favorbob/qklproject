package cc.stbl.token.innerdisc.restapi;

import cc.stbl.framework.protocol.provider.CodeDefinition;

public enum ResponseCode implements CodeDefinition {
    SUCCESS(1000,"操作成功"),
    ERROR(1100,"操作失败"),
    ERROR_PARAMS_ERROR(1101,"参数校验失败"),
    LOGIN_NO(1200,"请先登录"),
    LOGIN_USER_NOT_FOUND_ERROR(1201,"用户不存在"),
    LOGIN_PASSWD_ERROR(1202,"密码错误"),
    LOGIN_USER_LOCK_ERROR(1203,"用户已禁止登录"),
    LOGIN_PAY_PWD_ERROR(1204,"支付密码错误"),
    MOBILE_DUPLICATE_ERROR(1205,"手机号{}已被注册"),
    INVITE_CODE_NOT_FOUND_ERROR(1206,"找不到邀请码"),
    USER_RMD_PARTNER_NOT_FOUND(1207,"邀请码无效"),
    TRADE_USER_ASSET_LESS(1301,"区块结算中,请稍等一会再试！"),
    MINT_NOT_CAN(1350,"挖矿已经结束"),
    OPT_NOAUTH_ERROR(1210,"没有操作权限"),
    VERIFY_ERROR(1401,"验证码错误"),
    SMS_SENDING_ERROR(1402,"短信发送出错"),
    GEN_QRCODE_ERROR(1500,"生成二维码异常"),
    UPLOAD_QRCODE_ERROR(1501,"上传二维码异常"),

    //钱包
    NO_WALLET(1601,"未开通钱包"),
    NO_WALLET_FLOW(1602,"没有更多钱包明细"),
    WALLET_EXISTS(1603,"已经开通过钱包了"),
    WALLET_CREATE_ERROR(1604,"创建失败，生成keystore错误"),
    WALLET_NOT_EXISTS(1605,"{}用户未开通钱包"),

    LINKED_TRADE_ORDER_STATUS_ERROR(1701,"单据不允许操作"),
    LINKED_TRADE_ORDER_ASSET_LESS(1702,"单据资产不足"),
    LINKED_TRADE_BALANCE_LESS(1710,"当前账户余额不足"),
    LINKED_TRADE_ORDER_NOT_FOUND(1703,"挂单交易不存在"),
    LINKED_TRADE_LIMITED_SELF(1704,"交易失败，单据和购买人相同" ),
    LINKED_TRADE_RECORD_STATUS_HAS_PROCESS(1705,"单据已经处理"),
    LINKED_TRADE_RECORD_CANNEL_BUYER_TIPS(1706,"付款方已提醒收款，不能取消交易"),
    LINKED_TRADE_RECORD_NO_OPT_AUTH(1708,"非法操作"),
    LINKED_TRADE_CANNEL_HAS_PROCESS_RECORD(1709,"您的订单有交易未处理，不能取消" ),
    LINKED_TRADE_IS_DOWN(1799,"挂单平台已关闭"),
    USER_USED_TIME_ERROR(1800,"统计结束，请重新开始"),

    //消息
    MESSAGE_SEND_ERROR(1802,"消息发送失败"),
    // 交易记录
    TRADE_RECORD_NOT_EXIST(1900, "记录不存在"),
    // 应用
    RESOURCE_NOT_EXIST(2000, "资源不存在"),
    // 应用
    DATA_NOT_EXIST(2100, "找不到数据"),

    // 资产
    ASSET_ERROR(3001, "资产类型异常"),
    
    //卡数量不够
    CARD_NUM_ERROR(3002, "可用卡数量不够"),
    //用户不存在
    PHONE_NUM_ERROR(3003, "用户不存在"),
    
    //不可以重新激活
    ACTIVATE_USER_NOT_ALLOW(3004, "不可以重新激活"),
    
    //不可以夸级注册
    REGISTER_USER_NOT_ALLOW_1(3005, "不可以跨级注册"),
    
  //不可以夸级注册
    REGISTER_USER_NOT_ALLOW_2(3006, "上级用户没有激活"),
    REGISTER_USER_NOT_ALLOW_3(3007, "该位置已经存在用户"),
    
    RED_ENVELOPE_NOT_ALLOW(3008, "不可以重复领取红包"),
    USER_STATUS_NOT_NORMAL(3009, "用户状态不正常"),
    PAY_PASSWORD_ERROR(3010, "支付密码有误"),
    PAY_PASSWORD_UPDATE_ERROR(3011, "支付密码有误"),
 
    MJ_NOT_ENOUGH(3012, "MJ不够"),

    UN_KNOWN(-1000,"未知错误");
    public final int code;
    public final String msg;
    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.msg;
    }
}
