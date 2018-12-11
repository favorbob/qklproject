package cc.stbl.token.innerdisc.common.push.inter.model.paltform;

public enum DeviceType {
    ANDROID("android"),
    IOS("ios"),
    HTML5("html5"),
    WECHAT("wechat"),
    WINPHONE("winphone");

    private final String value;

    private DeviceType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}