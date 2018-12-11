package cc.stbl.token.innerdisc.common.remote.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class OSSUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OSSUtils.class);

    /**
     * 文件下载
     *
     * @param client
     * @param bucketName
     * @param fileName   上传到阿里云的文件名称
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */
    public InputStream downloadFile(OSSClient client, String bucketName, String fileName)
            throws OSSException, ClientException, IOException {
        OSSObject object = client.getObject(new GetObjectRequest(bucketName, fileName));
        if (object == null) {
            throw new FileNotFoundException(String.format("{bucket:%s, fileName:%s}", bucketName, fileName));
        }
        return object.getObjectContent();
    }

    /**
     * 向阿里云的OSS存储中存储文件 --file也可以用InputStream替代
     *
     * @param client     OSS客户端
     * @param is         上传文件
     * @param bucketName bucket名称
     * @return String 唯一MD5数字签名
     */
    public final String uploadFile(OSSClient client, InputStream is, String bucketName, String fileName) throws IOException {
        String resultStr = null;
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentDisposition("inline;filename=" + fileName);
        // 上传文件
        PutObjectResult putResult = client.putObject(bucketName, fileName, is, metadata);
        // 解析结果
        resultStr = putResult.getETag();
        LOGGER.info("上传阿里云OSS服务器响应结果." + resultStr);
        return fileName;
    }
    
    /**
     * 获得临时访问url .
     * @param key
     * @param expireAt
     * @return
     */
    public String getUrl(OSSClient client, String bucketName,String key, Long expireAt) {
        URL url = client.generatePresignedUrl(bucketName, key, new Date(expireAt));
        if (url != null) {
            return url.toString();
        }
        return null;
    }


}
