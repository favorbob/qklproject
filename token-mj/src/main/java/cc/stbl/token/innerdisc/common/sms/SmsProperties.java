package cc.stbl.token.innerdisc.common.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sms.aliyun")
@Component
public class SmsProperties {

    private String smsCodeKey;
    private boolean mock;
    private String accessKeyId;
    private String accessKeySecret;
    private String sign;

    public String getSmsCodeKey() {
        return smsCodeKey;
    }

    public void setSmsCodeKey(String smsCodeKey) {
        this.smsCodeKey = smsCodeKey;
    }

    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
