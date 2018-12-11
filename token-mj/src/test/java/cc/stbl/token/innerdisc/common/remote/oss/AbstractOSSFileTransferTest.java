package cc.stbl.token.innerdisc.common.remote.oss;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class AbstractOSSFileTransferTest extends AbstractTestCase{
    @Autowired
    private RemoteFileTransfer remoteFileTransfer;
    @Test
    public void upload() throws Exception {
        String uri = remoteFileTransfer.upload("/test", "text.txt", new ByteArrayInputStream("TEXT".getBytes(Charset.forName("utf8"))));
        System.err.println(uri);
    }

    @Test
    public void download() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        remoteFileTransfer.download("/test","text.txt", outputStream);
        System.out.println(new String(outputStream.toByteArray()));
    }

}