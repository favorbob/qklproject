package cc.stbl.token.innerdisc.restapi.app.common;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping(PathPrefix.API_PATH + "/fileTransfer")
@Api(description = "图片上传")
public class UploadFileRestController {


    @Autowired
    private RemoteFileTransfer remoteFileTransfer;

    @Autowired
    private OssProperties ossProperties;

    private final static String COMMON_PATH = "/upload/ucomm/";

    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    @ApiOperation(value = "上传文件")
    public Response<UploadFileProto.ResponseUpload> upload(@RequestParam MultipartFile file) throws Exception {
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId();
        String fileName = file.getOriginalFilename();
        String name = JavaUUIDGenerator.get() + trySubFileName(fileName);;
        String filePath = remoteFileTransfer.upload(path,name,file.getInputStream());
        filePath = "/" + filePath;
        UploadFileProto.ResponseUpload upload = new UploadFileProto.ResponseUpload();
        upload.setPath(filePath);
        upload.setName(name);
        upload.setFileSize(file.getSize());
        upload.setUrl(ossProperties.getDefault().getHost() + filePath);
        return Response.success(upload);
    }

    public  String trySubFileName(String fileName) {
        if(!fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }
}
