package cc.stbl.token.innerdisc.common.remote.oss;

import cc.stbl.token.innerdisc.common.remote.URI2L;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

public abstract class OSSURI2L implements URI2L {

    @Autowired
    private OssProperties ossProperties;

    protected String domain = null;

    abstract String getDomain();

    @PostConstruct
    protected void init(){
//        this.domain = ossProperties.getDefault().getHost();
        this.domain = getDomain();
        this.domain = this.domain.endsWith("/") ? this.domain : (this.domain + "/");
    }

    @Override
    public String uri2Url(String uri) {
        if (uri == null) {
            uri = "";
        }
        return domain + (uri.replaceFirst("^/", ""));
    }

    @Override
    public String url2Uri(String url) {
        if (url == null) {
            return "";
        }
        return url.replace(domain, "").replaceFirst("^/", "");
    }


    public String getOssObjectKey(String path, String name){
        Assert.isTrue(StringUtils.hasText(path) || StringUtils.hasText(name));
        path = path == null ? "" : path.trim();
        name = name == null ? "" : name.trim();
        String fileName = path + "/" + name;
        fileName = fileName.replaceAll("\\\\", "/").replaceAll("(?<!:)/+", "/");
        fileName = url2Uri(fileName);
        return fileName;
    }
}
