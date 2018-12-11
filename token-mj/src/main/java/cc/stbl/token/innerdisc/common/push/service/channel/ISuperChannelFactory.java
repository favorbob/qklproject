package cc.stbl.token.innerdisc.common.push.service.channel;

import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;
import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Developer on 2017-4-14.
 */
public interface ISuperChannelFactory {

    Collection<AbstractPushClientWrapper> createPushClientWrapper(String application, Set<DeviceType> deviceTypes);

    Collection<AbstractChannelConfig> createChannelConfig(String application);

    void register(String key, AbstractChannelFactory channelFactory);
}
