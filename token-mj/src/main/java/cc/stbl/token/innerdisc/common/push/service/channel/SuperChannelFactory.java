package cc.stbl.token.innerdisc.common.push.service.channel;

import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;
import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Developer on 2017-4-13.
 */
@Component
public class SuperChannelFactory implements ISuperChannelFactory {

    private final Map<String, AbstractChannelFactory> channelFactories = new HashMap<>();

    @Override
    public Collection<AbstractPushClientWrapper> createPushClientWrapper(String application, Set<DeviceType> deviceTypes) {

        Collection<AbstractPushClientWrapper> clientWrappers = new HashSet<>();
        channelFactories.values().parallelStream().forEach(channelFactory -> {
            AbstractPushClientWrapper clientWrapper = channelFactory.createPushClient(application, deviceTypes);
            if (clientWrapper != null)
                clientWrappers.add(clientWrapper);
        });
        return clientWrappers;
    }

    @Override
    public Collection<AbstractChannelConfig> createChannelConfig(String application){
        Collection<AbstractChannelConfig> configs = new HashSet<>();
        channelFactories.values().parallelStream().forEach(channelFactory -> {
            AbstractChannelConfig channelConfig = channelFactory.createChannelConfig(application);
            configs.add(channelConfig);
        });
        return configs;
    }

    @Override
    public void register(String key, AbstractChannelFactory channelFactory) {
        this.channelFactories.put(key, channelFactory);
    }
}
