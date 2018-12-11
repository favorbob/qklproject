package cc.stbl.token.innerdisc.common.push.service.channel.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.connection.HttpProxy;
import cn.jpush.api.JPushClient;
import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;
import cc.stbl.token.innerdisc.common.push.service.channel.AbstractChannelFactory;
import cc.stbl.token.innerdisc.common.push.service.channel.AbstractPushClientWrapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Developer on 2017-4-13.
 */
@Component
public class JPushChannelFactory extends AbstractChannelFactory {

    private static final String CHANNEL_KEY = "jpush";

    @Override
    protected String getChannelKey() {
        return CHANNEL_KEY;
    }

    @Override
    protected AbstractChannelConfig createChannelConfig(String application, Map<String, Object> channelConfig) {
        return new JPushChannelConfig(application, this.getChannelKey(), channelConfig);
    }

    @Override
    protected AbstractPushClientWrapper createPushClient(AbstractChannelConfig channelConfig) {
        JPushChannelConfig accessInfo = (JPushChannelConfig) channelConfig;
        String jPushAppKey = (String) accessInfo.getAppKey();
        String jPushMasterSecret = (String) accessInfo.getMasterSecretKey();
        HttpProxy httpProxy = null;
        JPushClient newClient = new JPushClient(jPushMasterSecret, jPushAppKey, httpProxy, ClientConfig.getInstance());
        JPushClientWrapper jPushClientWrapper = new JPushClientWrapper(accessInfo.getApplication(), CHANNEL_KEY, newClient);
        return jPushClientWrapper;
    }
}
