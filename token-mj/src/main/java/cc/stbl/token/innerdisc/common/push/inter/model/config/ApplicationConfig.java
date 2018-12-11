package cc.stbl.token.innerdisc.common.push.inter.model.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 2017-4-17.
 */
public class ApplicationConfig {
    private String application;
    private boolean enabled;
    //channel,
    private Map<String, BaseChannelConfig> channels = new HashMap<>();

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, BaseChannelConfig> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, BaseChannelConfig> channels) {
        this.channels = channels;
    }

    public void addChannels(String s, BaseChannelConfig cfg) {
        this.channels.put(s, cfg);
    }
}
