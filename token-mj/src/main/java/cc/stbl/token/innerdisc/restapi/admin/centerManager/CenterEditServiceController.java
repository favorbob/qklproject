package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditService;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditServicePic;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterEditServicePicService;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterEditServiceService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.common.AdminUploadFileRestController;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(PathPrefix.ADMIN_PATH + "/centerManager/editService")
@Api(description = "中心管理-客服编辑管理")
public class CenterEditServiceController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CenterEditServicePicService centerEditServicePicService;

    @Autowired
    private CenterEditServiceService centerEditServiceService;

    @Autowired
    private AdminUploadFileRestController adminUploadFileRestController;

    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录
    //private final static String COMMON_PATH = "E:/mnt/public/"; //本地测试地址

    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    @ApiOperation(value = "上传客服图片")
    public Response<String> upload( @RequestParam MultipartFile file, @RequestParam(required=false) String editId,CenterEditServiceProto.addEntity request) throws Exception {

        String filedataFileName = file.getOriginalFilename();
       // String path = request.getSession().getServletContext().getRealPath("imgupload/");
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
        //UUID改文件名，避免文件名重复
        String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
        String message;
        String err = "";
        String msg = path + newFileName;

        logger.info("客服图片上传信息msg:" +msg);
        try {
            FileUtil.uploadFile(file.getBytes(), path, newFileName);
        } catch (Exception e) {
            err = e.getMessage();
        }
        //返回给前端不包括 COMMON_PATH 路径
        message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                + "\"}";
        err = message;

        String serviceName = request.getServiceName();
        String remarks = request.getRemarks();
        logger.info(" serviceName:{},remarks:{}",serviceName,remarks);
        
        CenterEditServicePic servicePic = new CenterEditServicePic();
        servicePic.setServiceName(serviceName);
        servicePic.setEditServiceId("ServiceId");
        servicePic.setRemarks(remarks);
        servicePic.setPicUrl(msg.substring(COMMON_PATH.length() -1));
        centerEditServicePicService.addServicePic(servicePic);
        logger.info("客服图片上传完成");
        return Response.success("客服编辑图片上传成功" + err);
    }

    @RequestMapping(value = "/deleteServicePic",method = {RequestMethod.POST})
    @ApiOperation(value = "删除客服编辑图片")
    public Response<String> deleteServicePic(@RequestBody @Valid CenterEditServiceProto.ServiceId id) throws Exception {
        centerEditServicePicService.deleteServicePic(id.getId());
        logger.info("删除客服图片picId:" +id);
        return Response.success("客服编辑图片删除成功");
    }

    //此方法一次访问 客服标题+多个客服图片
    @RequestMapping(value = "/selectEditService",method = {RequestMethod.POST})
    @ApiOperation(value = "查询客服标题-一次返回客服标题和多个客服图片")
    public Response<List<CenterEditService>> selectEditService() throws Exception {
        List<CenterEditService> serviceslist = centerEditServiceService.selectEditServiceAndPicsResult();  //查询客服标题
        logger.info("查询客服标题成功，返回集合大小：" + serviceslist.size());
        return Response.success(serviceslist);
    }

    @RequestMapping(value = "/selectServiceTileAll",method = {RequestMethod.POST})
    @ApiOperation(value = "查询所有的客服标题list")
    public Response<List<CenterEditService>> selectServiceTileAll() throws Exception {
        List<CenterEditService> serviceslist = centerEditServiceService.findAll();  //查询所有的客服标题
        logger.info("查询所有的客服标题成功，返回集合大小：" + serviceslist.size());
        return Response.success(serviceslist);
    }

    @RequestMapping(value = "/selectServicePicPage",method = {RequestMethod.POST})
    @ApiOperation(value = "分页查询客服图片")
    public Response<Pager<CenterEditServicePic>> selectServicePicPage(
    		@RequestBody @Valid CenterEditServiceProto.ServicePage request) throws Exception {
        CenterEditServicePic object = new CenterEditServicePic();
        Pager<CenterEditServicePic> serviceslist = centerEditServicePicService.findPage(object, request.getPageNo(), request.getPageSize());       
        
        logger.info("分页查询客服图片，返回集合大小：" + serviceslist.getTotalCount());
        return Response.success(serviceslist);
    }

    @RequestMapping(value = "/addHeading",method = {RequestMethod.POST})
    @ApiOperation(value = "添加客服标题")
    public Response<String> addHeading(@RequestBody @Valid CenterEditServiceProto.ESRequestList editService) throws Exception {
        CenterEditService editService12 = new CenterEditService();
        BeanUtils.copyProperties(editService, editService12);
        editService12.setId(JavaUUIDGenerator.get());
        editService12.setCreateDate(new Date());

        centerEditServiceService.add(editService12);
        logger.info("添加客服标题成功");
        return Response.success("添加客服标题成功");
    }

    @RequestMapping(value = "/deleteHeading",method = {RequestMethod.POST})
    @ApiOperation(value = "删除客服标题")
    public Response<String> dealHeading(@RequestBody @Valid CenterEditServiceProto.Id id) throws Exception {
        centerEditServiceService.delete(id.getId());
        logger.info("删除客服标题成功");
        return Response.success("删除客服标题成功");
    }
}
