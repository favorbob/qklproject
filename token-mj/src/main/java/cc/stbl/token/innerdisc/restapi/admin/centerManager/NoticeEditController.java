package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEdit;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEditUser;
import cc.stbl.token.innerdisc.modules.centerManager.service.NoticeEditService;
import cc.stbl.token.innerdisc.modules.centerManager.service.NoticeEditUserService;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.sys.SysUserProto;
import cc.stbl.token.innerdisc.restapi.admin.user.VrUserProto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(PathPrefix.ADMIN_PATH + "/centerManager/noticeedit")
@Api(description = "中心管理-公告管理")
public class NoticeEditController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NoticeEditUserService noticeEditUserService;

    @Autowired
    private NoticeEditService noticeEditService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    private final static String COMMON_PATH = "/mnt/public/";  //服务文件上传、下载基础目录
    //private final static String COMMON_PATH = "E:/mnt/public/"; //本地测试地址
    /**
    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    @ApiOperation(value = "上传简介公告多媒体")
    public Response<String> upload(HttpServletRequest request, @RequestParam MultipartFile file) throws Exception {

        String filedataFileName = file.getOriginalFilename();
       // String path = request.getSession().getServletContext().getRealPath("imgupload/");
        String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId() + "/";
        //UUID改文件名，避免文件名重复
        String newFileName = JavaUUIDGenerator.get()+filedataFileName.substring(filedataFileName.indexOf("."),filedataFileName.length());
        String message;
        String err = "";
        String msg = path + newFileName;

        logger.info("公告多媒体信息msg:" +msg);
        try {
            FileUtil.uploadFile(file.getBytes(), path, newFileName);
        } catch (Exception e) {
            err = e.getMessage();
        }
        //返回给前端不包括 COMMON_PATH 路径
        message = "{\"err\":\"" + err + "\",\"msg\":\"" + msg.substring(COMMON_PATH.length() -1)
        + "\"}";
        err = message;
        logger.info("公告多媒体上传成功");
        return Response.success("公告多媒体上传成功" + err);
    }
    */

    @RequestMapping(value = "/deleteNoticeEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "删除公告")
    public Response<String> deleteIntroEdit(@RequestBody @Valid NoticeEditProto.IdAndNum ids) throws Exception {
        noticeEditService.delete(ids.getId());   //删除公告
        logger.info("删除公告id:" +ids.getId());
        noticeEditUserService.deleteByNoticeConsignee(ids.getConsigneeNum());
        logger.info("删除收件人，收件人编码:" +ids.getConsigneeNum());
        return Response.success("删除公告成功");
    }

    @RequestMapping(value = "/addNoticeEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "添加公告")
    public Response<String> addIntroEdit(@RequestBody @Valid NoticeEditProto.NoticeEditPro condition) throws Exception {
        NoticeEdit introEdit = new NoticeEdit();
        BeanUtils.copyProperties(condition, introEdit);
        String msg = noticeEditService.insertNoticeEdit(introEdit);
        logger.info(msg);
        return Response.success(msg);
    }

    @RequestMapping(value = "/updateNoticeEdit",method = {RequestMethod.POST})
    @ApiOperation(value = "更新公告")
    public Response<String> updateIntroEdit(@RequestBody @Valid NoticeEditProto.NoticeEditProALL condition) throws Exception {
        NoticeEdit introEdit = new NoticeEdit();
        BeanUtils.copyProperties(condition, introEdit);

        String msg = noticeEditService.updateNoticeEdit(introEdit); //更新操作
        logger.info(msg);
        return Response.success(msg);
    }

    @RequestMapping(value = "/deployNotice",method = {RequestMethod.POST})
    @ApiOperation(value = "发布公告")
    public Response<String> deployNotice(@RequestBody @Valid NoticeEditProto.Id condition) throws Exception {
        NoticeEdit introEdit = new NoticeEdit();
        BeanUtils.copyProperties(condition, introEdit);
        String msg = noticeEditService.deployNotice(introEdit); //发布操作
        logger.info(msg);
        return Response.success(msg);
    }

    //获取分页公告（不包含公告内容）
    @RequestMapping(value = {"/getPageNoticeEdits"},method = RequestMethod.POST)
    @ApiOperation("获取公告list")
    public Response<Pager<NoticeEdit>> getPageIntroEdits(@RequestBody NoticeEditProto.NoticeEditProPage condition) {
        NoticeEdit object = new NoticeEdit();
        BeanUtils.copyProperties(condition, object);
        Pager<NoticeEdit> page = noticeEditService.findPageNotContext(object, condition.getPageNo(), condition.getPageSize());
        logger.info("获取公告list成功");
        return Response.success(page);
    }
    //根据id获取公告详情（包含公告内容）
    @RequestMapping(value = {"/getNoticeDetail"},method = RequestMethod.POST)
    @ApiOperation("获取公告详情")
    public Response<NoticeEdit> getNoticeDetail(@RequestBody NoticeEditProto.Id condition) {
        NoticeEdit object = new NoticeEdit();
        BeanUtils.copyProperties(condition, object);
        NoticeEdit noticeEdit = new NoticeEdit();
        noticeEdit =  noticeEditService.get(condition.getId()) ;
        logger.info("获取公告详情成功");
        return Response.success(noticeEdit);
    }

    @RequestMapping(value = "/addUpdateNoticeUser",method = {RequestMethod.POST})
    @ApiOperation(value = "添加/更新收件人")
    public Response<String> addUpdateNoticeUser(@RequestBody @Valid NoticeEditProto.NoticeUserPro condition) throws Exception {

        String msg = noticeEditUserService.insertAndUpdateNoticeUser(
                condition.getConsigneeNum(), condition.getUserId(), condition.getUserName());
        logger.info(msg);
        return Response.success(msg);
    }

    @RequestMapping(value = "/searchNoticeUser",method = {RequestMethod.POST})
    @ApiOperation(value = "查看收件人列表")
    public Response<List<NoticeEditUser>> searchNoticeUser(@RequestBody @Valid NoticeEditProto.ConsigneeNum condition) throws Exception {
        List<NoticeEditUser> liste = noticeEditUserService.findUseListByConsignee(condition.getConsigneeNum());
        logger.info("获取查看收件人list成功");
        return Response.success(liste);
    }
    @RequestMapping(value = "/searchUserList",method = {RequestMethod.POST})
    @ApiOperation(value = "查找收件人-姓名模糊查询")
    public Response<Pager<VrUserInfo>> searchUserList(@RequestBody @Valid VrUserProto.UserNameQuery condition) throws Exception {
        Pager<VrUserInfo> pageUser = vrUserInfoService.findUserListByName(condition.getUserName(), condition.getPageNo(), condition.getPageSize());
        logger.info("获取查看收件人list成功");
        return Response.success(pageUser);
    }

}
