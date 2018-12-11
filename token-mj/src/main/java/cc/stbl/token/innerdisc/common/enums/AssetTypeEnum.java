package cc.stbl.token.innerdisc.common.enums;

import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;

import java.util.HashMap;
import java.util.Map;

public enum AssetTypeEnum {
    AVAILABLE(TradeConsts.FLOW_TYPE_BALANCE, "可用资产"),
    LIMITED(TradeConsts.FLOW_TYPE_LIMITED_BALANCE, "受限资产"),
    ;
    AssetTypeEnum(Integer code, String name) {
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

    private static Map<Integer, AssetTypeEnum> codeMap;
    static {
        codeMap = new HashMap<Integer, AssetTypeEnum>();
        for (AssetTypeEnum type : AssetTypeEnum.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    public static AssetTypeEnum getByCode(Integer code) {
        AssetTypeEnum type = codeMap.get(code);
        if (type != null) {
            return type;
        }
        return null;
    }
}
