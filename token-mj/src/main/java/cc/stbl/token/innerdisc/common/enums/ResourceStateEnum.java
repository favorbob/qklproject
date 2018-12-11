package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResourceStateEnum {
    NOT_NO_THE_SHOP(0, "待上架"),
    NO_THE_SHOP(1, "已上架"),
    ALREADY_DOWN(2, "已下架"),
    ;
    ResourceStateEnum(Integer code, String name) {
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

    private static Map<Integer, ResourceStateEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, ResourceStateEnum>();
        for (ResourceStateEnum type : ResourceStateEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        ResourceStateEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
