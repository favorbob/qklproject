package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum BuyingCategoryEnum {
    //1：解锁前(游戏)，2：解锁后(游戏)，0：默认(电影用)
    VIDEO(0, "默认(电影用)"),
    GAME_BEFORE_UNLOCKING(1, "解锁前(游戏"),
    GAME_AFTER_UNLOCKING(2, "解锁后(游戏)"),
    ;
    BuyingCategoryEnum(Integer code, String name) {
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

    private static Map<Integer, BuyingCategoryEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, BuyingCategoryEnum>();
        for (BuyingCategoryEnum type : BuyingCategoryEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        BuyingCategoryEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
