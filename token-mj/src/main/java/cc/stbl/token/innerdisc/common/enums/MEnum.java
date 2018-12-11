package cc.stbl.token.innerdisc.common.enums;

public enum  MEnum {
    LOGIN_MINT(1,"登录积分返利挖矿"),
    CONSUME_MINT(2,"消费挖矿");

    public final Integer code;
    public final String remark;
    MEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
