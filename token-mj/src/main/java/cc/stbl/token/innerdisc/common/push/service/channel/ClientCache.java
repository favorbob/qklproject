package cc.stbl.token.innerdisc.common.push.service.channel;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 2017-4-14.
 */
@Component
public class ClientCache {
    //application, <channel, client>
    private static Map<String, Map<String, CacheableClient>> cache = new HashMap<>();

    public static Map<String, CacheableClient> get(String application){
        return cache.get(application);
    }
    public static CacheableClient get(String application, String channel){
        Map<String, CacheableClient> applicationClient = cache.get(application);
        if(applicationClient == null){
            return null;
        }
        return applicationClient.get(channel);
    }

    public void put(String application, String channelKey, CacheableClient<? extends AbstractPushClientWrapper> cacheableClient) {
        Map<String, CacheableClient> applicationClient = cache.get(application);
        if(applicationClient == null){
            synchronized (cache){
                if((applicationClient = cache.get(application)) == null){
                    applicationClient = new HashMap<>();
                    cache.put(application, applicationClient);
                }
            }
        }
        applicationClient.put(channelKey, cacheableClient);
    }

    public static class CacheableClient<T> {
        private int ver;
        private boolean expired;
        private T client;
        public boolean expired(int ver){
            return expired ? true : (expired = (this.ver > ver));
        };
        public CacheableClient(T client, int ver){
            this.client = client;
            this.ver = ver;
        }

        public T getClient() {
            return client;
        }
    }
}
