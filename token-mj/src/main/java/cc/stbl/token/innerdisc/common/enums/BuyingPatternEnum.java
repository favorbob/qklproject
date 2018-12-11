package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum BuyingPatternEnum {
    CHANGE(1, "零钱"),
    ASSET(2, "资产"),
    ;
    BuyingPatternEnum(Integer code, String name) {
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

    private static Map<Integer, BuyingPatternEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, BuyingPatternEnum>();
        for (BuyingPatternEnum type : BuyingPatternEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        BuyingPatternEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
