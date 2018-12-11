package cc.stbl.token.innerdisc.restapi.app.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroEdit;
import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroPerson;
import cc.stbl.token.innerdisc.modules.centerManager.service.IntroEditService;
import cc.stbl.token.innerdisc.modules.centerManager.service.IntroPersionService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.IntroEditProto;
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

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/centerHome/introedit")
@Api(description = "中心管理-简介查询")
public class AppIntroEditController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IntroEditService introEditService;

    @Autowired
    private IntroPersionService introPersionService;

    //获取分页简介公告
    @RequestMapping(value = {"/getPageList"},method = RequestMethod.POST)
    @ApiOperation("获取简介公告list")
    public Response<Pager<IntroEdit>> getPageList(@RequestBody IntroEditProto.IntroEditProAPPPage condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能使用！");
        }
        IntroEdit introEdit = new IntroEdit();
        BeanUtils.copyProperties(condition, introEdit);
        Pager<IntroEdit> page = introEditService.findPage(introEdit, condition.getPageNo(), condition.getPageSize());
        logger.info("获取简介公告list成功");
        return Response.success(page);
    }

    //根据id获取简介详情（包含公告内容）
    @RequestMapping(value = {"/getIntroDetail"},method = RequestMethod.POST)
    @ApiOperation("根据id获取简介详情")
    public Response<IntroEdit> getIntroDetail(@RequestBody IntroEditProto.Id condition) {
        IntroEdit object = new IntroEdit();
        BeanUtils.copyProperties(condition, object);
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能获取简介详情！");
        }
        IntroEdit noticeEdit = new IntroEdit();
        noticeEdit =  introEditService.get(condition.getId()) ;
        logger.info("获取简介详情成功");
        return Response.success(noticeEdit);
    }

    @RequestMapping(value = {"/getIntroAllRead"},method = RequestMethod.POST)
    @ApiOperation("获取简介list-包含已读未读")
    public Response<Pager<IntroEdit>> getIntroAllRead(@RequestBody IntroEditProto.IntroEditProAPPPage condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能查询个人公告！");
        }
        logger.info("获取简介userId" + userId);
        //获取个人公告-分页
        Pager<IntroEdit> page = introEditService.findIntroAllReadPage(userId, condition.getPageNo(), condition.getPageSize());
        logger.info("获取简介list成功");
        return Response.success(page);
    }

    @RequestMapping(value = "/updateIntro",method = {RequestMethod.POST})
    @ApiOperation(value = "更新简介")
    public Response<String> updateIntro(@RequestBody @Valid IntroEditProto.IntroPersonData condition) throws Exception {
        IntroPerson object = new IntroPerson();
        BeanUtils.copyProperties(condition, object);
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能更新系统公告！");
        }
        object.setUserId(userId);   //设置userId
        String msg = introPersionService.addOrUpdateIntroPerson(object);
        logger.info(msg);
        return Response.success(msg);
    }

}
