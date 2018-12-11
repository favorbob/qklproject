//package cc.stbl.token.innerdisc.restapi.app.user;
//
//import cc.stbl.framework.protocol.provider.Response;
//import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
//import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
//import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
//import cc.stbl.token.innerdisc.restapi.PathPrefix;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping(value = {PathPrefix.API_PATH + "/register"})
//@Api(description = "注册服务")
//public class UserRegisterController {
//    @Autowired
//    private VrUserInfoService userInfoService;
//    @Autowired
//    private SmsVerifyService smsVerifyService;
//
//    @RequestMapping(method = RequestMethod.POST,value = "")
//    @ApiOperation("注册")
//    public Response register(@RequestBody @Valid UserRegisterProto.RequestRegister request){
//        VrUserInfo sysUser = new VrUserInfo();
//        BeanUtils.copyProperties(request,sysUser);
//        sysUser.setUserLevel(VrUserInfo.USER_LEVEL_GENERAL);
//        try{
//            //校验验证码
//            smsVerifyService.verifySmsCode("0",request.getPhoneNum(),request.getCode());
//            //创建用户
//            userInfoService.createNewUser(sysUser,sysUser.getInviteCode());
//        }catch (Exception e){
//            return Response.error(e.getMessage());
//        }
//        return Response.success(sysUser.getUserId());
//    }
//}
