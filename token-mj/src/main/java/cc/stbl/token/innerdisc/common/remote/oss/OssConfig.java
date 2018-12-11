package cc.stbl.token.innerdisc.common.remote.oss;

import com.aliyun.oss.OSSClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ OssProperties.class })
public class OssConfig {

    @Bean
    public OSSClient defaultOssClient(OssProperties ossProperties) {
        OSSClient ossClient = new OSSClient(ossProperties.getDefault().getEndpoint(), ossProperties.getDefault().getAccessKeyId(), ossProperties.getDefault().getSecretAccessKey());
        return ossClient;
    }

    @Bean("defaultOssFileTransfer")
    public AbstractOSSFileTransfer defaultOssFileTransfer(final OssProperties ossProperties) {
        return new AbstractOSSFileTransfer() {
            @Override
            public String getBucket() {
                return ossProperties.getDefault().getBucket();
            }

            @Override
            public OSSClient getOssClient() {
                return defaultOssClient(ossProperties);
            }

            @Override
            String getDomain() {
                return ossProperties.getDefault().getHost();
            }
        };
    }

    @Bean("defaultOssFileManager")
    public AbstractOSSFileManager defaultOssFileManager(OssProperties ossProperties) {
        return new AbstractOSSFileManager() {

            @Override
            public String getBucket() {
                return ossProperties.getDefault().getBucket();
            }

            @Override
            public OSSClient getOssClient() {
                return defaultOssClient(ossProperties);
            }

            @Override
            String getDomain() {
                return ossProperties.getDefault().getHost();
            }
        };
    }


   /* @Bean
    public OSSClient publicReadResourceOssClient(OssProperties ossProperties) {
        OSSClient ossClient = new OSSClient(ossProperties.getPublicReadResource().getEndpoint(), ossProperties.getPublicReadResource().getAccessKeyId(),
                ossProperties.getPublicReadResource().getSecretAccessKey());
        return ossClient;
    }

    @Bean
    public OSSClient privateResourceOssClient(OssProperties ossProperties) {
        OSSClient ossClient = new OSSClient(ossProperties.getPrivateResource().getEndpoint(), ossProperties.getPrivateResource().getAccessKeyId(),
                ossProperties.getPrivateResource().getSecretAccessKey());
        return ossClient;
    }*/

//    @Bean("ossPrivateFileTransfer")
//    public AbstractOSSFileTransfer ossPrivateFileTransfer(final OssProperties ossProperties) {
//        return new AbstractOSSFileTransfer() {
//            @Override
//            public String getBucket() {
//                return ossProperties.getPrivateResource().getBucket();
//            }
//
//            @Override
//            public OSSClient getOssClient() {
//                return privateResourceOssClient(ossProperties);
//            }
//
//            @Override
//            String getDomain() {
//                return ossProperties.getPrivateResource().getHost();
//            }
//        };
//    }
//
//    @Bean("ossPrivateFileManager")
//    public AbstractOSSFileManager ossPrivateFileManager(OssProperties ossProperties) {
//        return new AbstractOSSFileManager() {
//
//            @Override
//            public String getBucket() {
//                return ossProperties.getPrivateResource().getBucket();
//            }
//
//            @Override
//            public OSSClient getOssClient() {
//                return privateResourceOssClient(ossProperties);
//            }
//
//            @Override
//            String getDomain() {
//                return ossProperties.getPrivateResource().getHost();
//            }
//        };
//    }
//
//    @Bean("ossPublicReadFileTransfer")
//    public AbstractOSSFileTransfer ossPublicReadFileTransfer(OssProperties ossProperties) {
//        return new AbstractOSSFileTransfer() {
//            @Override
//            public String getBucket() {
//                return ossProperties.getPublicReadResource().getBucket();
//            }
//
//            @Override
//            public OSSClient getOssClient() {
//                return publicReadResourceOssClient(ossProperties);
//            }
//
//            @Override
//            String getDomain() {
//                return ossProperties.getPublicReadResource().getHost();
//            }
//        };
//    }
//
//    @Bean("ossPublicReadFileManager")
//    public AbstractOSSFileManager ossPublicReadFileManager(OssProperties ossProperties) {
//        return new AbstractOSSFileManager() {
//
//            @Override
//            public String getBucket() {
//                return ossProperties.getPublicReadResource().getBucket();
//            }
//
//            @Override
//            public OSSClient getOssClient() {
//                return publicReadResourceOssClient(ossProperties);
//            }
//
//            @Override
//            String getDomain() {
//                return ossProperties.getPublicReadResource().getHost();
//            }
//        };
//    }

}
