package cc.stbl.token.innerdisc.common.push.service.channel.jpush;

import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;

import java.util.Map;

/**
 * Created by Developer on 2017-4-13.
 */
public class JPushChannelConfig extends AbstractChannelConfig {

    private String appKey;
    private String masterSecretKey;

    public JPushChannelConfig(String application, String channelKey, Map<String, Object> channelConfig) {
        super(application, channelKey, channelConfig);
        try {
            org.apache.commons.beanutils.BeanUtils.populate(this, channelConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMasterSecretKey() {
        return masterSecretKey;
    }

    public void setMasterSecretKey(String masterSecretKey) {
        this.masterSecretKey = masterSecretKey;
    }
}
