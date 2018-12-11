package cc.stbl.token.innerdisc.common.remote;

import java.util.List;

public interface RemoteFileManager{

    /**
     * 列出目录下文件/目录
     * @param url
     * @return
     */
    List<RemoteFile> list(String url);

    /**
     * 加载文件。无法加载目录
     * @param url
     * @return
     */
    RemoteFile getFile(String url);

    /**
     * 加载文件。无法加载目录
     * @param url
     * @return
     */
    RemoteFile getFile(String url, long expiredAt);

    boolean makeFile(String url);

    boolean deleteFile(String url);

    boolean rename(String oriUrl, String targetUrl);
}
