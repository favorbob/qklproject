package cc.stbl.token.innerdisc.common.remote.oss;

import com.aliyun.oss.OSSClient;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractOSSFileTransfer extends OSSURI2L implements RemoteFileTransfer {

    protected OSSClient ossClient;
    @Autowired
    protected OSSUtils ossUtils;

    @Autowired
    protected OssProperties ossProperties;
    protected String bucket;

    public abstract String getBucket();
    public abstract OSSClient getOssClient();

    @PostConstruct
    protected void init(){
        super.init();
//        this.bucket = ossProperties.getDefault().getBucket();
        this.bucket = getBucket();
        this.ossClient = getOssClient();
    }

    @Override
    public String upload(String path, String name, InputStream inputStream) throws IOException {
        String ossObjectKey = this.getOssObjectKey(path, name);
        return ossUtils.uploadFile(this.ossClient, inputStream, this.bucket, ossObjectKey);
    }

    @Override
    public void download(String path, String name, OutputStream outputStream) throws IOException {
        String ossObjectKey = this.getOssObjectKey(path, name);
        InputStream inputStream = ossUtils.downloadFile(this.ossClient, bucket, ossObjectKey);
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        outputStream.flush();
    }

//    public static void main(String[] args) throws IOException, URISyntaxException {
//        String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";
//        String accessKeyId = "LTAItUXEXDTIiZcy";
//        String secretAccessKey="R1ZzNkJBgNuMFUDfECQgtG5fqDZcug";
//        String bucket = "mall-resources";
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, secretAccessKey);
//        OSSFileTransfer manager = new OSSFileTransfer();
//        manager.ossClient = ossClient;
//        manager.bucket = bucket;
//        manager.domain = "https://mall-resources.oss-cn-shenzhen.aliyuncs.com";
//        manager.ossUtils = new OSSUtils();
//        manager.init();
//        FileOutputStream fos = new FileOutputStream("d:/abc");
//        manager.download(null,"https://mall-resources.oss-cn-shenzhen.aliyuncs.com/user/avatar.png",fos);
//        System.err.println();
//    }

    public void setOssClient(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
