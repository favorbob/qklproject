package cc.stbl.token.innerdisc.common.push.service.access;

import cc.stbl.token.innerdisc.common.push.inter.model.config.BaseChannelConfig;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;

import java.util.Map;

/**
 * Created by Developer on 2017-4-13.
 */
public abstract class AbstractChannelConfig extends BaseChannelConfig {

    protected final Map<String, Object> channelConfig;

    public AbstractChannelConfig(String application,String channelKey, Map<String, Object> channelConfig){
        super();
        this.channelConfig = channelConfig;
        super.application = application;
        super.name = channelKey;
        Map deviceMap = (Map) channelConfig.get("devices");
        if(deviceMap != null){
            deviceMap.values().forEach(o -> deviceTypes.add(DeviceType.valueOf(o.toString().toUpperCase())));
        }
        Boolean enabled = (Boolean) channelConfig.get("enabled");
        super.enabled = enabled == null ? false : enabled;
        Boolean cacheable= (Boolean) channelConfig.get("cacheable");
        super.cachable = cacheable == null ? false : cacheable;
        Integer ver = (Integer) channelConfig.get("ver");
        super.ver = ver == null ? 0 : ver;
    }

    public Map<String, Object> getChannelConfig() {
        return channelConfig;
    }

}
