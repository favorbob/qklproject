package cc.stbl.token.innerdisc.common.push.inter.model.config;

import cc.stbl.token.innerdisc.common.push.inter.model.paltform.DeviceType;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Developer on 2017-4-17.
 */
public class BaseChannelConfig {
    protected String name;
    protected String application;
    protected Collection<DeviceType> deviceTypes = new HashSet<>();
    protected boolean enabled;
    protected boolean cachable;
    protected int ver;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(Collection<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCachable() {
        return cachable;
    }

    public void setCachable(boolean cachable) {
        this.cachable = cachable;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
