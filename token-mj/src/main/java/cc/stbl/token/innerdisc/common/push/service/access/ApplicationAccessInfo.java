package cc.stbl.token.innerdisc.common.push.service.access;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 2017-4-13.
 */
public class ApplicationAccessInfo {
    private Boolean enabled;
    private Map<String, Map<String, Object>> channels = new HashMap<>();

    public Map<String, Map<String, Object>> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, Map<String, Object>> channels) {
        this.channels = channels;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
