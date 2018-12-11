package cc.stbl.token.innerdisc.common.push.service.channel;

import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;
import cc.stbl.token.innerdisc.common.push.service.access.AbstractChannelConfig;
import cc.stbl.token.innerdisc.common.push.service.access.ApplicationAccessInfo;
import cc.stbl.token.innerdisc.common.push.service.access.ApplicationAccessInfoManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Developer on 2017-4-13.
 */
public abstract class AbstractChannelFactory {

    private final static Object LOCK = new Object();

    @Autowired
    private ISuperChannelFactory superChannelFactory;

    @Autowired
    private ApplicationAccessInfoManager applicationAccessInfoManager;

    @Autowired
    private ClientCache clientCache;

    protected abstract String getChannelKey();

    public AbstractChannelConfig createChannelConfig(String application) {
        String channelKey = this.getChannelKey();
        ApplicationAccessInfo applicationAccessInfo = getApplicationAccessInfo(application);
        if (applicationAccessInfo == null) {
            return null;
        }
        Map<String, Object> channelConfig = applicationAccessInfo.getChannels().get(channelKey);
        if (channelConfig == null) {
            return null;
        }
        AbstractChannelConfig cc = this.createChannelConfig(application, channelConfig);
        return cc;
    }


    public AbstractPushClientWrapper createPushClient(String applicationKey, Set<DeviceType> target) {
        String channelKey = this.getChannelKey();
        AbstractChannelConfig accessInfo = this.createChannelConfig(applicationKey);
        if(accessInfo == null || !accessInfo.isEnabled() || !this.suitable(target, accessInfo.getDeviceTypes())){
            return null;
        }
        int ver = accessInfo.getVer();
        if (ver != 0) {//客户端可以缓存？
            AbstractPushClientWrapper client = this.getAbstractPushClientWrapperFromCache(applicationKey, channelKey, ver);
            if (client != null) return client;
        }

        if (accessInfo.isEnabled()) {
            AbstractPushClientWrapper pushClientWrapper = this.createPushClient(accessInfo);
            Collection<DeviceType> deviceTypes = accessInfo.getDeviceTypes();
            if (deviceTypes != null)
                deviceTypes.parallelStream().forEach(deviceType -> pushClientWrapper.getSuitDeviceTypes().add(deviceType));
            if (ver != 0) {
                synchronized (LOCK) {
                    AbstractPushClientWrapper client = this.getAbstractPushClientWrapperFromCache(applicationKey, channelKey, ver);
                    if (client == null) {
                        ClientCache.CacheableClient<AbstractPushClientWrapper> jPushClientCacheableClient = new ClientCache.CacheableClient<>(pushClientWrapper, ver);
                        clientCache.put(applicationKey, channelKey, jPushClientCacheableClient);
                    }
                }
            }
            return pushClientWrapper;
        }
        return null;
    }

    private ApplicationAccessInfo getApplicationAccessInfo(String application) {
        return applicationAccessInfoManager.getApplications().get(application);
    }

    @PostConstruct
    private void register() {
        superChannelFactory.register(this.getChannelKey(), this);
    }

    private AbstractPushClientWrapper getAbstractPushClientWrapperFromCache(String applicationKey, String channelKey, int ver) {
        ClientCache.CacheableClient client = getCacheableClient(applicationKey, channelKey);
        if (client != null)
            if (!client.expired(ver)) {
                return (AbstractPushClientWrapper) client.getClient();
            } else {// 手动清除缓存还是等待覆盖清除？
                //等待覆盖清除
            }
        return null;
    }

    protected boolean suitable(Collection<DeviceType> target, Collection<DeviceType> cfg) {
        if(cfg == null || cfg.isEmpty())
            return false;
        if(target == null || target.isEmpty()) {
            return true;
        }
        return target.parallelStream().anyMatch(o -> cfg.contains(o));
    }

    private ClientCache.CacheableClient getCacheableClient(String applicationKey, String channelKey) {
        return clientCache.get(applicationKey, channelKey);
    }

    protected abstract AbstractPushClientWrapper createPushClient(AbstractChannelConfig channelConfig);

    protected abstract AbstractChannelConfig createChannelConfig(String application, Map<String, Object> channelConfig);

}
