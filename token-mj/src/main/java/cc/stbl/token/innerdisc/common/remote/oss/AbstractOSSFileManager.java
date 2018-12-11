package cc.stbl.token.innerdisc.common.remote.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import cc.stbl.token.innerdisc.common.remote.RemoteFile;
import cc.stbl.token.innerdisc.common.remote.RemoteFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOSSFileManager extends OSSURI2L implements RemoteFileManager {
    private static final Logger logger = LoggerFactory.getLogger(AbstractOSSFileManager.class);
    @Autowired
    protected OSSClient ossClient;

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    protected OssProperties ossProperties;

    protected String bucket;

    public abstract String getBucket();
    public abstract OSSClient getOssClient();

    @PostConstruct
    protected void init(){
        super.init();
        this.bucket = this.getBucket();
        this.ossClient = this.getOssClient();
    }

    @Override
    public List<RemoteFile> list(String url) {
        String uri = this.url2Uri(url);
        Map<String, RemoteFile> set = new HashMap<>();
        List<RemoteFile> list = new ArrayList<>();
        Object[] objects = this.listAll(url);
        List<String> commonPrefixes = (List<String>) objects[0];
        List<OSSObjectSummary> ossObjectSummaries = (List<OSSObjectSummary>) objects[1];

        for (OSSObjectSummary objectSummary : ossObjectSummaries) {
            if (objectSummary.getKey().equals(uri))
                continue;
            RemoteFile remoteFile = toFile(objectSummary);
            list.add(remoteFile);
        }
        for (String commonPrefix : commonPrefixes) {
            RemoteFile remoteFile = toDir(commonPrefix);
            list.add(remoteFile);
        }

        return list;
    }

    private RemoteFile toDir(String commonPrefix) {
        String name = commonPrefix.replaceAll("/$", "");
        name = name.substring(name.lastIndexOf("/") + 1);
        RemoteFile remoteFile = new RemoteFile();
        remoteFile.setName(name);
        remoteFile.setUrl(this.uri2Url(commonPrefix));
        remoteFile.setUri(commonPrefix);
        remoteFile.setLastModified(null);
        remoteFile.setLength(0);
        remoteFile.setDirectory(true);
        remoteFile.setFile(false);
        return remoteFile;
    }

    private RemoteFile toFile(OSSObjectSummary objectSummary) {
        String key = objectSummary.getKey();
        String name = key.substring(key.lastIndexOf("/") + 1);
        RemoteFile remoteFile = new RemoteFile();
        remoteFile.setName(name);
        remoteFile.setUrl(this.uri2Url(key));
        remoteFile.setUri(key);
        remoteFile.setLastModified(objectSummary.getLastModified());
        remoteFile.setLength(objectSummary.getSize());
        remoteFile.setDirectory(false);
        remoteFile.setFile(true);
        return remoteFile;
    }

    private Object[] listAll(String dir) {
        List<OSSObjectSummary> ossObjectSummaryList = new ArrayList<OSSObjectSummary>();
        List<String> commonPrefixesList = new ArrayList<String>();
        // 查询
        ObjectListing objectListing = null;
        String nextMarker = null;
        final int maxKeys = 1000;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucket)
                    .withPrefix(dir).withDelimiter("/").withMarker(nextMarker).withMaxKeys(maxKeys);
            objectListing = this.ossClient.listObjects(listObjectsRequest);
            List<OSSObjectSummary> summaryList = objectListing.getObjectSummaries();
            List<String> commonPrefixes = objectListing.getCommonPrefixes();
            ossObjectSummaryList.addAll(summaryList);
            commonPrefixesList.addAll(commonPrefixes);
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        return new Object[]{commonPrefixesList, ossObjectSummaryList};
    }

    @Override
    public RemoteFile getFile(String url, long expiredAt) {
        RemoteFile file = this.getFile(url);
        if (expiredAt > 0 && file.isFile()) {
            file.setUrl(ossUtils.getUrl(this.ossClient, this.bucket, file.getUri(), expiredAt));
        }
        return file;
    }

    @Override
    public RemoteFile getFile(String url) {
        String uri = this.url2Uri(url);
        OSSObject object = null;
        try {
            object = ossClient.getObject(this.bucket, uri);
            if (object == null)
                return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
        RemoteFile file = new RemoteFile();
        file.setUrl(this.uri2Url(uri));
        file.setUri(uri);
        file.setLength(object.getObjectMetadata().getContentLength());
        file.setLastModified(object.getObjectMetadata().getLastModified());
        boolean isFile = !uri.endsWith("/");
        file.setFile(isFile);
        file.setDirectory(!isFile);
        String name = uri;
        if (!isFile) {
            name = name.replaceAll("/$", "");
        }
        name = name.replaceAll(".*/", "");
        file.setName(name);
        return file;
    }

    public boolean makeFile(String url) {
        if (!url.endsWith("/")) {
            url += "/";
        }
        String uri = url2Uri(url);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ossClient.putObject(this.bucket, uri, inputStream);
        return true;
    }

    @Override
    public boolean deleteFile(String url) {
        String uri = url2Uri(url);
        ObjectListing objectListing = ossClient.listObjects(this.bucket, uri);
        if (objectListing == null) {
            return false;
        }
        List<String> keys = new ArrayList<>();
        for (OSSObjectSummary object : objectListing.getObjectSummaries()) {
            keys.add(object.getKey());
        }
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(this.bucket);
        deleteObjectsRequest.setKeys(keys);
        ossClient.deleteObjects((DeleteObjectsRequest) deleteObjectsRequest);
        return true;
    }

    @Override
    public boolean rename(String oriUrl, String targetUrl) {
        throw new RuntimeException("oss rename not supported");
    }

    public static void main(String[] args) {
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        String accessKeyId = "LTAIDUY7TjxxHJNC";
        String secretAccessKey = "USFbEeRU27qMv1I6z3ttQeBKtoY4D3";
        String bucket = "mall-goods";
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, secretAccessKey);
        AbstractOSSFileManager manager = new AbstractOSSFileManager(){

            @Override
            public RemoteFile getFile(String url, long expiredAt) {
                return null;
            }

            @Override
            String getDomain() {
                return null;
            }

            @Override
            public String getBucket() {
                return null;
            }

            @Override
            public OSSClient getOssClient() {
                return null;
            }
        };
        manager.ossClient = ossClient;
        manager.bucket = bucket;
        manager.domain = "http://mall-goods.oss-cn-shenzhen.aliyuncs.com";
        manager.init();
        List<RemoteFile> list = null;
        RemoteFile file = manager.getFile("image/20180308/20180308185105_482.png");
        manager.makeFile("images/mydir/m/y/d/i/r");
        System.err.println(list);
    }
}
