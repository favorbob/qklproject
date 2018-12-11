package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResourceTypeEnum {
    GAME(1, "游戏"),
    VIDEO(2, "电影"),
    ;
    ResourceTypeEnum(Integer code, String name) {
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

    private static Map<Integer, ResourceTypeEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, ResourceTypeEnum>();
        for (ResourceTypeEnum type : ResourceTypeEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        ResourceTypeEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
