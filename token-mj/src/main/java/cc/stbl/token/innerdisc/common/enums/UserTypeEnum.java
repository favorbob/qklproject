package cc.stbl.token.innerdisc.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserTypeEnum {
    SYS(1, "系统用户"),
    TEAMLEADER(2, "团队领导"),
    GENERAL(9, "会员"),
    ;
    UserTypeEnum(Integer code, String name) {
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

    private static Map<Integer, UserTypeEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, UserTypeEnum>();
        for (UserTypeEnum type : UserTypeEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static String getNameByCode(Integer code) {
        UserTypeEnum type = codeMap.get(code);
        if (type != null) {
            return type.getName();
        }
        return "";
    }
}
