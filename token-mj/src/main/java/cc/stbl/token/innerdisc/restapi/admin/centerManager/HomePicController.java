package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterHomePic;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterHomePicService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.common.AdminUploadFileRestController;
import cc.stbl.token.innerdisc.restapi.admin.common.UploadFileProto;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@RestController
@RequestMapping(PathPrefix.ADMIN_PATH + "/centerManager/homePic")
@Api(description = "中心管理-首页图片")
public class HomePicController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CenterHomePicService centerHomePicService;

    @Autowired
    private AdminUploadFileRestController adminUploadFileRestController;

    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录
    //private final static String COMMON_PATH = "E:/mnt/public/"; //本地测试地址

    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    @ApiOperation(value = "上传文件")
    public Response<String> upload(HttpServletRequest request, @RequestParam MultipartFile file, @RequestParam(required=false) String outurl) throws Exception {
       /* Response<UploadFileProto.ResponseUpload> ups =  adminUploadFileRestController.upload(file) ;    //上传图片
        UploadFileProto.ResponseUpload upload = ups.getData();
        CenterHomePic homePic = new CenterHomePic();
        homePic.setPicName(upload.getName());
        homePic.setOutUrl(outurl);
        homePic.setPicUrl(upload.getUrl());*/

        String filedataFileName = file.getOriginalFilename();
       // String path = request.getSession().getServletContext().getRealPath("imgupload/");
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
System.out.println("path--->" + path);
        //UUID改文件名，避免文件名重复
        String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
        String message;
        String err = "";
        String msg = path + newFileName;
System.out.println("msg--->" + msg);
        logger.info("首页图片上传信息msg:" +msg);
        try {
            FileUtil.uploadFile(file.getBytes(), path, newFileName);
        } catch (Exception e) {
            err = e.getMessage();
        }
        //返回给前端不包括 COMMON_PATH 路径
        message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                + "\"}";
        err = message;

        CenterHomePic homePic = new CenterHomePic();
        homePic.setPicName(newFileName);
        homePic.setOutUrl(outurl);
        homePic.setPicUrl(msg.substring(COMMON_PATH.length() -1));

        centerHomePicService.addHomePic(homePic);
        logger.info("首页图片上传成功");
        return Response.success("首页图片上传成功" + err);
    }

    //首页图片分页
    @RequestMapping(value = {"/getPagePics"},method = RequestMethod.POST)
    @ApiOperation("获取首页图片list")
    public Response<Pager<CenterHomePic>> list(@RequestBody CenterHomePicProto.PicRequestList condition) {
        CenterHomePic homePic = new CenterHomePic();
        BeanUtils.copyProperties(condition, homePic);
        Pager<CenterHomePic> page = centerHomePicService.findPageByquey(homePic, condition.getPageNo(), condition.getPageSize());
        return Response.success(page);
    }

    @RequestMapping(value = "/delete",method = {RequestMethod.POST})
    @ApiOperation(value = "删除首页图片")
    public Response<String> deleteById(@RequestBody CenterHomePicProto.PicId picId) throws Exception {
        centerHomePicService.deleteHomePic(picId.getPicId(), "");
        return Response.success("删除首页图片成功");
    }

    @RequestMapping(value = "/updateSorts",method = {RequestMethod.POST})
    @ApiOperation(value = "更新首页图片排序")
    public Response<String> updateSorts(@RequestBody CenterHomePicProto.IdSorts idSorts) throws Exception {
        String msg = centerHomePicService.updatePicSortByIds(idSorts.getIds(),idSorts.getPicSorts());
        return Response.success(msg);
    }
}
