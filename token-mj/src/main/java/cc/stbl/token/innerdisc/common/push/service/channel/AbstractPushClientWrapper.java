package cc.stbl.token.innerdisc.common.push.service.channel;

import cc.stbl.token.innerdisc.common.push.inter.model.ChannelPushResult;
import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Developer on 2017-4-11.
 */
public abstract class AbstractPushClientWrapper {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPushClientWrapper.class);
    protected final String channelKey;

    protected final String application;

    public String getChannelKey() {
        return channelKey;
    }

    protected AbstractPushClientWrapper(String application, String channelKey){
        this.channelKey = channelKey;
        this.application = application;
    }

    protected final Collection<DeviceType> suitDeviceTypes = new HashSet<>();

    protected abstract ChannelPushResult push(PushPayload payload);

    public ChannelPushResult doPush(PushPayload payload){
        StopWatch sw = new StopWatch(getChannelKey() + "[" + payload.getSendno() + "]");
        sw.start(getChannelKey() + "[" + payload.getSendno() + "]");
        try {
            return this.push(payload);
        } finally {
            sw.stop();
            logger.debug(sw.prettyPrint());
        }
    };

    public Collection<DeviceType> getSuitDeviceTypes() {
        return suitDeviceTypes;
    }

    public String getApplication() {
        return application;
    }
}
