package cc.stbl.token.innerdisc.common.remote;

import java.io.InputStream;
import java.io.OutputStream;

public interface RemoteFileTransfer extends URI2L {

    /**
     * @param path
     * @param name
     * @param inputStream
     * @return uri
     * @throws Exception
     */
    String upload(String path, String name, InputStream inputStream) throws Exception;

    /**
     * @param path
     * @param name
     * @param outputStream
     * @throws Exception
     */
    void download(String path, String name, OutputStream outputStream) throws Exception;


}
