package cc.stbl.token.innerdisc.restapi.app.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterHomePic;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterHomePicService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.CenterHomePicProto;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import cc.stbl.token.innerdisc.restapi.admin.common.AdminUploadFileRestController;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/centerHome/homePic")
@Api(description = "中心管理-app首页图片")
public class AppHomePicController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CenterHomePicService centerHomePicService;

    @Autowired
    private AdminUploadFileRestController adminUploadFileRestController;

    //首页图片list
    @RequestMapping(value = {"/getPicsList"},method = RequestMethod.POST)
    @ApiOperation("获取首页图片list")
    public Response<List<CenterHomePic>> getPicsList(@RequestBody CenterHomePicProto.PicRequestListAddPage condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能使用！");
        }
        CenterHomePic homePic = new CenterHomePic();
        BeanUtils.copyProperties(condition, homePic);
        List<CenterHomePic> centerHomePicList = centerHomePicService.findList(homePic);
        return Response.success(centerHomePicList);
    }

    //首页图片list
    @RequestMapping(value = {"/getPicsPage"},method = RequestMethod.POST)
    @ApiOperation("获取首页图片Page")
    public Response<List<CenterHomePic>> getPicsPage(@RequestBody CenterHomePicProto.PicRequestList condition) {
        String userId = ShiroUtils.getLoginUserId();
        if( "".equals(userId.trim()) ) {
            return Response.error("用户必须登录后才能使用！");
        }
        CenterHomePic homePic = new CenterHomePic();
        BeanUtils.copyProperties(condition, homePic);
        List<CenterHomePic> centerHomePicList = centerHomePicService.findList(homePic);
        return Response.success(centerHomePicList);
    }

}
