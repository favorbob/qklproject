package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum StatusEnum {
    NORMAL(0, "正常"),
    ABNORMAL(1, "异常"),
    ;
    StatusEnum(Integer code, String name) {
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

    private static Map<Integer, StatusEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, StatusEnum>();
        for (StatusEnum type : StatusEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        StatusEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
