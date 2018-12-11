package cc.stbl.token.innerdisc.restapi.app.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NotReadMsgCount;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEdit;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEditUser;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticePerson;
import cc.stbl.token.innerdisc.modules.centerManager.service.*;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.NoticeEditProto;
import cc.stbl.token.innerdisc.restapi.admin.sys.SysUserProto;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/centerHome/noticeEdit")
@Api(description = "中心管理-app公告管理")
public class AppNoticeEditController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NoticeEditUserService noticeEditUserService;

    @Autowired
    private NoticeEditService noticeEditService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private NoticePersionService noticePersionService;

    @Autowired
    private IntroEditService introEditService;

    @RequestMapping(value = "/selectNoticeInfo",method = {RequestMethod.POST})
    @ApiOperation(value = "查询公告详情")
    public Response<NoticeEdit> selectNoticeInfo(@RequestBody @Valid NoticeEditProto.Id id) throws Exception {
        logger.info("查询公告详情id:" +id.getId());
        NoticeEdit noticeEdit = noticeEditService.get(id.getId());
        logger.info("查询公告详情成功！");
        return Response.success(noticeEdit);
    }

    @RequestMapping(value = {"/getSysNoticePage"},method = RequestMethod.POST)
    @ApiOperation("获取系统公告list-不含已读标识")
    public Response<Pager<NoticeEdit>> getSysNoticePage(@RequestBody NoticeEditProto.NoticeEditNullPage condition) {
        NoticeEdit object = new NoticeEdit();
        BeanUtils.copyProperties(condition, object);
        object.setMsgType(1);   //1-系统消息，2-个人消息
        object.setStatue(9);    //公告状态 1-新增，2-修改，3-删除，9-生效
        //获取系统公告
        Pager<NoticeEdit> page = noticeEditService.findPage(object, condition.getPageNo(), condition.getPageSize());
        logger.info("获取系统公告list成功");
        return Response.success(page);
    }

    @RequestMapping(value = {"/getUserNoticePage"},method = RequestMethod.POST)
    @ApiOperation("获取个人公告list")
    public Response<Pager<NoticeEdit>> getUserNoticePage(@RequestBody NoticeEditProto.NoticeEditNullPage condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能查询个人公告！");
        }
        logger.info("获取个人公告的userId" + userId);
        //获取个人公告-分页
        Pager<NoticeEdit> page = noticeEditService.findUserNoticePage(userId, condition.getPageNo(), condition.getPageSize());
        logger.info("获取个人公告list成功");
        return Response.success(page);
    }

    @RequestMapping(value = "/updatePersonNotice",method = {RequestMethod.POST})
    @ApiOperation(value = "更新个人公告")
    public Response<String> updatePersonNotice(@RequestBody @Valid NoticeEditProto.NoticeConNum condition) throws Exception {
        NoticeEditUser noticeUser = new NoticeEditUser();
        BeanUtils.copyProperties(condition, noticeUser);
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能查询个人公告！");
        }
        noticeUser.setUserId(userId);   //设置用户id
        String msg = noticeEditUserService.updatePersonNotice(noticeUser); //更新个人公告为已读
        logger.info(msg);
        return Response.success(msg);
    }
    //根据id获取公告详情（包含公告内容）
    @RequestMapping(value = {"/getNoticeDetail"},method = RequestMethod.POST)
    @ApiOperation("根据id获取公告详情")
    public Response<NoticeEdit> getNoticeDetail(@RequestBody NoticeEditProto.Id condition) {
        NoticeEdit object = new NoticeEdit();
        BeanUtils.copyProperties(condition, object);
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能获取公告详情！");
        }
        NoticeEdit noticeEdit = new NoticeEdit();
        noticeEdit =  noticeEditService.get(condition.getId()) ;
        logger.info("获取公告详情成功");
        return Response.success(noticeEdit);
    }

    @RequestMapping(value = {"/getSysNoticeAllRead"},method = RequestMethod.POST)
    @ApiOperation("获取系统公告list-包含已读未读")
    public Response<Pager<NoticeEdit>> getSysNoticeAllRead(@RequestBody NoticeEditProto.NoticeEditNullPage condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能查询个人公告！");
        }
        logger.info("获取系统公告的userId" + userId);
        //获取个人公告-分页
        Pager<NoticeEdit> page = noticeEditService.findSysNoticeAllReadPage(userId, condition.getPageNo(), condition.getPageSize());
        logger.info("获取系统公告list成功");
        return Response.success(page);
    }

    @RequestMapping(value = "/updateSysNotice",method = {RequestMethod.POST})
    @ApiOperation(value = "更新系统公告")
    public Response<String> updateSysNotice(@RequestBody @Valid NoticeEditProto.NoticePersonData condition) throws Exception {
        NoticePerson object = new NoticePerson();
        BeanUtils.copyProperties(condition, object);
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能更新系统公告！");
        }
        object.setUserId(userId);   //设置userId
        String msg = noticePersionService.addOrUpdateNoticePerson(object);
        logger.info(msg);
        return Response.success(msg);
    }

    @RequestMapping(value = "/getSysNotReadCount",method = {RequestMethod.POST})
    @ApiOperation(value = "获取个人系统消息-简介未读总数")
    public Response<NotReadMsgCount> getSysNotReadCount() throws Exception {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能更新系统公告！");
        }
        NotReadMsgCount notReadMsgCount = new NotReadMsgCount();
        int sysMsgNum  = 0;
        int introMsgNum  = 0;
        int useNum = 0;

        introMsgNum = introEditService.getIntroPersonNotRead(userId);
        logger.info("获取个人简介消息未读总数成功，总数为" + introMsgNum);

        sysMsgNum = noticeEditService.getSysPersonNotRead(userId); //个人系统消息未读总数
        logger.info("获取个人系统公告未读总数成功，总数为" + sysMsgNum);

        useNum =  noticeEditService.findUserNotReadCount(userId); //个人公告未读总数
        logger.info("获取个人公告未读总数成功，总数为" + useNum);

        notReadMsgCount.setIntroMsgCount(String.valueOf(introMsgNum));
        notReadMsgCount.setSysMsgCount(String.valueOf( sysMsgNum + useNum )); //返回系统公告+ 个人公告 未读总数
        return Response.success(notReadMsgCount);
    }

    @RequestMapping(value = {"/getNotReadNoticeOne"},method = RequestMethod.POST)
    @ApiOperation("获取一条系统公告/个人公告")
    public Response<List<NoticeEdit>> getNotReadNoticeOne() {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能查询个人公告！");
        }
        logger.info("获取系统公告/个人公告的userId" + userId);
        //获取系统公告、个人公告 1条记录
        List<NoticeEdit> list2 = noticeEditService.getNotReadNoticeOne( userId );
        logger.info("获取一条系统公告/个人公告成功！");
        return Response.success(list2);
    }
}
