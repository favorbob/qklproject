package cc.stbl.token.innerdisc.common.push.inter.model.paltform;

import cc.stbl.token.innerdisc.common.push.inter.model.Preconditions;

import java.util.HashSet;
import java.util.Set;

public class Platform  {
    private static final String ALL = "all";
    
    private boolean all;
    private Set<DeviceType> deviceTypes;

    private Platform(){
        super();
    }

    private Platform(boolean all, Set<DeviceType> deviceTypes) {
        this.all = all;
        this.deviceTypes = deviceTypes;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static Platform all() {
        return newBuilder().setAll(true).build();
    }
    
    public static Platform android() {
        return newBuilder().addDeviceType(DeviceType.ANDROID).build();
    }
    
    public static Platform ios() {
        return newBuilder().addDeviceType(DeviceType.IOS).build();
    }

    public static Platform html5() {
        return newBuilder().addDeviceType(DeviceType.HTML5).build();
    }
    
    public static Platform winphone() {
        return newBuilder().addDeviceType(DeviceType.WINPHONE).build();
    }

    public static Platform wechat() {
        return newBuilder().addDeviceType(DeviceType.WECHAT).build();
    }
    
    public static Platform android_ios() {
        return newBuilder()
                .addDeviceType(DeviceType.ANDROID)
                .addDeviceType(DeviceType.IOS)
                .build();
    }
    
    public static Platform android_winphone() {
        return newBuilder()
                .addDeviceType(DeviceType.ANDROID)
                .addDeviceType(DeviceType.WINPHONE)
                .build();
    }
    
    public static Platform ios_winphone() {
        return newBuilder()
                .addDeviceType(DeviceType.IOS)
                .addDeviceType(DeviceType.WINPHONE)
                .build();
    }
    
    public boolean isAll() {
        return all;
    }

    public Set<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public static class Builder {
        private boolean all;
        private Set<DeviceType> deviceTypes;
        
        public Builder setAll(boolean all) {
            this.all = all;
            return this;
        }
        
        public Builder addDeviceType(DeviceType deviceType) {
            if (null == deviceTypes) {
                deviceTypes = new HashSet<DeviceType>();
            }
            deviceTypes.add(deviceType);
            return this;
        }
        
        public Platform build() {
            Preconditions.checkArgument(! (all && null != deviceTypes), "Since all is enabled, any platform should not be set.");
            Preconditions.checkArgument(! (!all && null == deviceTypes), "No any deviceType is set.");
            return new Platform(all, deviceTypes);
        }
    }
    
}


