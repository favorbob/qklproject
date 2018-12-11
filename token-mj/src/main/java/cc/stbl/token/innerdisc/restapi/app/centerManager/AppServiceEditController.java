package cc.stbl.token.innerdisc.restapi.app.centerManager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditService;
import cc.stbl.token.innerdisc.modules.centerManager.entity.CenterEditServicePic;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterEditServicePicService;
import cc.stbl.token.innerdisc.modules.centerManager.service.CenterEditServiceService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.CenterEditServiceProto;
import cc.stbl.token.innerdisc.restapi.admin.centerManager.FileUtil;
import cc.stbl.token.innerdisc.restapi.admin.common.AdminUploadFileRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ks.common.datastore.model.Pager;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/centerHome/serviceEdit")
@Api(description = "中心管理-app客服编辑")
public class AppServiceEditController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CenterEditServicePicService centerEditServicePicService;

    @Autowired
    private CenterEditServiceService centerEditServiceService;

    @Autowired
    private AdminUploadFileRestController adminUploadFileRestController;

    @RequestMapping(value = "/selectEditService",method = {RequestMethod.POST})
    @ApiOperation(value = "查询客服标题")
    public Response selectEditService(Integer pageNo,Integer pageSize) throws Exception {
       
      List<CenterEditServicePic> serviceslist = centerEditServicePicService.findAll();       
      
      return Response.success(serviceslist);
    }
}
