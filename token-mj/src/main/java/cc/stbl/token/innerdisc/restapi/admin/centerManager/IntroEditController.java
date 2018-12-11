package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.util.DateUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroEdit;
import cc.stbl.token.innerdisc.modules.centerManager.service.IntroEditService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
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
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(PathPrefix.ADMIN_PATH + "/centerManager/introedit")
@Api(description = "中心管理-简介编辑管理")
public class IntroEditController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IntroEditService introEditService;

    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录
    //private final static String COMMON_PATH = "E:/mnt/public/"; //本地测试地址


    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    @ApiOperation(value = "上传简介多媒体")
    public Response<String> upload(HttpServletRequest request, @RequestParam MultipartFile file) throws Exception {
        String filedataFileName = file.getOriginalFilename();
       // String path = request.getSession().getServletContext().getRealPath("imgupload/");
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
        //UUID改文件名，避免文件名重复
        String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
        String message;
        String err = "";
        String msg = path + newFileName;

        logger.info("简介多媒体信息msg:" +msg);
        try {
            int f50MB = 10485760;  //50 * 1024 * 1024  10MB
            if(file.getBytes().length > f50MB ) {
                logger.info("文件上传失败，文件应小于10MB");
                return Response.error("文件上传失败，文件应小于10MB");
            }
            FileUtil.uploadFile(file.getBytes(), path, newFileName);
        } catch (Exception e) {
            err = e.getMessage();
        }
        //返回给前端不包括 COMMON_PATH 路径
        message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                + "\"}";
        err = message;
        logger.info("简介多媒体上传成功");
        return Response.success("简介多媒体上传成功" + err);
    }

//    暂时不用这个上传功能
//    @RequestMapping(value = "/uploadFile",method = {RequestMethod.POST})
//    @ApiOperation(value = "上传附件")
    public Response<String> uploadFile(HttpServletRequest request, @RequestParam MultipartFile file) throws Exception {

        String filedataFileName = file.getOriginalFilename();
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
        //文件名+时分秒.jpg
        String preName  = filedataFileName.substring(0, filedataFileName.indexOf("."));
        String shifenMiao = DateUtils.getHourMinuSecStr(); //获取时分秒
        String endName = filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
        String newFileName = preName + "-" + shifenMiao + endName ; //文件名xxx-192221.jpg
        String message;
        String err = "";
        String msg = path + newFileName;

        logger.info("上传附件msg:" +msg);
        try {
            int f50MB = 10485760;  //50 * 1024 * 1024  10MB
            if(file.getBytes().length > f50MB ) {
                logger.info("文件上传失败，文件应小于10MB");
                return Response.error("文件上传失败，文件应小于10MB");
            }
            FileUtil.uploadFile(file.getBytes(), path, newFileName);
        } catch (Exception e) {
            err = e.getMessage();
        }
        //返回给前端不包括 COMMON_PATH 路径
        message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
                + "\"}";
        err = message;
        logger.info("附件上传成功");
        return Response.success("附件上传成功" + err);
    }

    @RequestMapping(value = "/deleteIntroEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "删除简介")
    public Response<String> deleteIntroEdit(@RequestBody @Valid IntroEditProto.Id id) throws Exception {
        introEditService.delete(id.getId());
        logger.info("删除简介id:" +id);
        return Response.success("删除简介成功");
    }

    @RequestMapping(value = "/addIntroEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "添加简介")
    public Response<String> addIntroEdit(@RequestBody @Valid IntroEditProto.IntroEditPro condition) throws Exception {
        IntroEdit introEdit = new IntroEdit();
        BeanUtils.copyProperties(condition, introEdit);
        introEdit.setId(JavaUUIDGenerator.get());
        introEdit.setCreateDate(new Date());

        introEditService.add(introEdit);
        logger.info("添加简介成功");
        return Response.success("添加简介成功");
    }

    @RequestMapping(value = "/updateIntroEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "更新简介")
    public Response<String> updateIntroEdit(@RequestBody @Valid IntroEditProto.IntroEditProALL condition) throws Exception {
        IntroEdit introEdit = new IntroEdit();
        BeanUtils.copyProperties(condition, introEdit);
        introEditService.update(introEdit); //更新操作
        logger.info("更新简介成功");
        return Response.success("更新简介成功");
    }

    //获取分页简介信息（不包含简介内容）
    @RequestMapping(value = {"/getPageIntroEdits"},method = RequestMethod.POST)
    @ApiOperation("获取简介list不含简介内容")
    public Response<Pager<IntroEdit>> getPageIntroEdits(@RequestBody IntroEditProto.IntroEditProPage condition) {
        IntroEdit introEdit = new IntroEdit();
        BeanUtils.copyProperties(condition, introEdit);
        Pager<IntroEdit> page = introEditService.findPageNotContext(introEdit, condition.getPageNo(), condition.getPageSize());
        logger.info("获取简介list成功");
        return Response.success(page);
    }

    //根据id获取简介详情（包含公告内容）
    @RequestMapping(value = {"/getIntroDetail"},method = RequestMethod.POST)
    @ApiOperation("获取简介详情")
    public Response<IntroEdit> getIntroDetail(@RequestBody IntroEditProto.Id condition) {
        IntroEdit object = new IntroEdit();
        BeanUtils.copyProperties(condition, object);
        IntroEdit noticeEdit = new IntroEdit();
        noticeEdit =  introEditService.get(condition.getId()) ;
        logger.info("获取简介详情成功");
        return Response.success(noticeEdit);
    }

}
