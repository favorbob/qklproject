package cc.stbl.token.innerdisc.common.remote.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "oss")
@Component
public class OssProperties {

    private Properties Default = new Properties();
    private Properties publicReadResource = new Properties();
    private Properties privateResource = new Properties();

    public Properties getDefault() {
        return Default;
    }

    public Properties getPublicReadResource() {
        return publicReadResource;
    }

    public Properties getPrivateResource() {
        return privateResource;
    }

    public void setDefault(Properties aDefault) {
        Default = aDefault;
    }

    public static class Properties{
        private String endpoint;
        private String accessKeyId;
        private String secretAccessKey;
        private String bucket;
        private String host;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecretAccessKey() {
            return secretAccessKey;
        }

        public void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}