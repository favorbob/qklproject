package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserImgTypeEnum {
    WECHAT(1, "微信收款码"),
    ALI_PAY(2, "支付宝收款码"),
    ETH_ASSET(3, "资产接收码"),
    ;
    UserImgTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static Map<Integer, UserImgTypeEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, UserImgTypeEnum>();
        for (UserImgTypeEnum type : UserImgTypeEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        UserImgTypeEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
