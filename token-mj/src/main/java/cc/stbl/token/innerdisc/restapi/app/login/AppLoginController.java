package cc.stbl.token.innerdisc.restapi.app.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.exception.StructWithCodeException;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.StatusEnum;
import cc.stbl.token.innerdisc.common.enums.UserImgTypeEnum;
import cc.stbl.token.innerdisc.common.enums.UserTypeEnum;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserImg;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLoginLog;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserImgService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLoginLogService;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.payment.service.MyWalletService;
import cc.stbl.token.innerdisc.modules.scheduler.PrizeScheduler;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.common.RandomValidateCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping(value = {PathPrefix.API_PATH })
@RestController
@Api(description = "登录")
public class AppLoginController {

    private static Logger logger = LoggerFactory.getLogger(AppLoginController.class);

	// @Autowired
	// PrizeScheduler scheduler;
    @Autowired
    private VrUserInfoService userInfoService;
    @Autowired
    private SmsVerifyService smsVerifyService;
    @Autowired
    private VrUserLoginLogService vrUserLoginLogService;
    @Autowired
    private VrUserImgService vrUserImgService;
    @Autowired
    private OssProperties ossProperties;
    @Autowired
    private MyWalletService myWalletService;
    
    @Autowired
    private EthWalletService walletService;
    
    @RequestMapping(value = "/scheduler",method = RequestMethod.GET)
    public void testExecute() {
		// scheduler.execute();
	}
    
    @RequestMapping(value = "/updatePassword",method = RequestMethod.GET)
    public void updatePassword() {
    	List<VrUserInfo> list = userInfoService.findAll();
    	for(VrUserInfo v:list) {
    		if(v.getNewPassword().equals("123456")) {
    		String payPassword = v.getPayPassword();
    		v.setPayPassword(DESUtil.encryptString(payPassword));
    		String newPassword = v.getNewPassword();
    		v.setNewPassword(DESUtil.encryptString(newPassword));
    		userInfoService.update(v);
    		}
    	}
	}
    
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Response login(@RequestBody @Valid AppLoginProto.RequestLogin request){
        try {
            logger.info("login_user={}",request.getMobile());
            Subject subject = SecurityUtils.getSubject();
            UserPwdToken token = new UserPwdToken();
            token.setUsername(request.getMobile());
            token.setPassword(request.getPassword().toCharArray());
            token.setUserType(LoginUser.UserType.API);
            token.setRememberMe(request.getRememberMe());
            logger.info("login(token)");
            subject.login(token);

            logger.info("login_success_user={}",request.getMobile());
            AppLoginProto.ResponseLogin responseLogin = new AppLoginProto.ResponseLogin();
            responseLogin.setRememberMe(request.getRememberMe());
            responseLogin.setToken(subject.getSession().getId().toString());
            VrUserInfo userInfo= userInfoService.get(ShiroUtils.getLoginUserId());
            if(userInfo.getStatus() != VrUserInfo.USER_STATUS_NORMAL) {
            	throw new StructWithCodeException(ResponseCode.USER_STATUS_NOT_NORMAL);
            }
            
            //更新设备号
            userInfo.setDeviceId(request.getDeviceId());
           // userInfoService.update(userInfo);
            responseLogin.setPhoneNum(userInfo.getPhoneNum());
            responseLogin.setUserName(userInfo.getUserName());
            responseLogin.setUID(userInfo.getInviteCode());
            responseLogin.setUserType(UserTypeEnum.getNameByCode(userInfo.getUserLevel()));
            String host = ossProperties.getDefault().getHost();
            String userIcon = userInfo.getUserIcon();
            if (!userIcon.startsWith("/")){
                userIcon = "/" + userIcon;
            }
            responseLogin.setUserIcon(host + userIcon);
            logger.info("login_success_getUserIcon_user={}",request.getMobile());
            VrUserImg vrUserImg = vrUserImgService.getByUserId(userInfo.getUserId(), 1, UserImgTypeEnum.ETH_ASSET.getCode());
            if (vrUserImg != null){
                String imgUrl = vrUserImg.getImgUrl();
                if (!imgUrl.startsWith("/")){
                    imgUrl = "/" + imgUrl;
                }
                responseLogin.setEthReceiptCodeImg(host + imgUrl);
            }
            logger.info("myWalletService");
            responseLogin.setHasWallet(myWalletService.hasEthWallet(userInfo.getUserId())?"1":"0");
            logger.info("login_success_hasWallet={}`user={}",responseLogin.getHasWallet(),request.getMobile());
            vrUserLoginLogService.add(new VrUserLoginLog(ShiroUtils.getLoginUserId(), StatusEnum.NORMAL));
            responseLogin.setUserId(ShiroUtils.getLoginUserId());
            return Response.success(responseLogin);
        }  catch (CredentialsException e) {
            return Response.bulid(ResponseCode.LOGIN_PASSWD_ERROR);
        } catch (AuthenticationException e) {
            Throwable cases = e.getCause();
            while (cases != null) {
                if(cases instanceof StructWithCodeException) {
                    return ((StructWithCodeException)cases).toResponse();
                }
                cases = cases.getCause();
            }
            e.printStackTrace();
            return Response.bulid(ResponseCode.ERROR);
        }
    }

    @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    @ApiOperation("登出")
    public Response logOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.success();
    }
    
    @RequestMapping(value = "/updateWalletPassword",method = RequestMethod.POST)
    @ApiOperation("更新区块链密码")
    public void updateWalletPassword(String phoneNum,String oldPassword,String payPassword){
    	VrUserInfo user = userInfoService.findByPhoneNum(phoneNum);
    	walletService.updatePasswordByUserId(user.getUserId(), oldPassword, payPassword);
//    	List<VrUserInfo> list = userInfoService.findAll();
//    	logger.info("==============userInfo 大小:{}",list.size());
//    	for(VrUserInfo v:list) {
//    		String userId = v.getUserId();
//    		String oldPassword =  "234567";
////    		if("13333333777".equals(v.getPhoneNum())) {
////    			oldPassword = "4E9AEB195AD3C978";
////    		}
//    		String payPassword = v.getPayPassword();
//    		try {
//				payPassword = DESUtil.decryptString(payPassword);
//				payPassword = "2345678";
//				logger.info("========="+v.getPhoneNum()+oldPassword+","+payPassword);
//				walletService.updatePasswordByUserId(userId, oldPassword, payPassword);
//			} catch (Exception e) {
//				
//			}
//    	}
    }
    
    

    @RequestMapping(value = "/regist",method = RequestMethod.POST)
    @ApiOperation("注册")
    public Response regist(@RequestBody @Valid AppLoginProto.RequestRegister request){
	    	VrUserInfo sysUser = new VrUserInfo();
	    	try{
	        BeanUtils.copyProperties(request,sysUser);
	        sysUser.setUserLevel(VrUserInfo.USER_LEVEL_GENERAL);
	        String phoneNum = request.getPhoneNum();
	        VrUserInfo userInfo = userInfoService.getUserByPhoneNum(phoneNum);
	        if(userInfo != null) throw new StructWithCodeException(ResponseCode.MOBILE_DUPLICATE_ERROR,request.getPhoneNum());
	       
	        if(!phoneNum.equals(VrUserInfo.TOP_USER)) {
		        //不可以跨级注册
		        VrUserInfo parentVrUserInfo = userInfoService.get(request.getParentUserId());
				if(parentVrUserInfo == null) {
					throw new StructWithCodeException(ResponseCode.REGISTER_USER_NOT_ALLOW_1);
				}
				
				//上级用户没有激活
				if(parentVrUserInfo.getStatus() == VrUserInfo.USER_STATUS_INACTIVE) {
					throw new StructWithCodeException(ResponseCode.REGISTER_USER_NOT_ALLOW_2);
				}
			      //校验验证码
				String topUserId = ShiroUtils.getLoginUserId();
				VrUserInfo isTopUser = userInfoService.get(topUserId);
				if(isTopUser.getPhoneNum().equals(VrUserInfo.TOP_USER)) {
					if(request.getSmsCode() == null || !request.getSmsCode().equals("000000")) {
						throw new StructWithCodeException(ResponseCode.VERIFY_ERROR);
					}
				}else {
					smsVerifyService.verifySmsCode("0",request.getPhoneNum(),request.getSmsCode());
				}
	        }

			 //创建用户
            userInfoService.registerUser(sysUser,request.getParentUserId(),request.getArea());
	            
           
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
        return Response.success(sysUser.getUserId());
    }
    
    
    String randomStr = "abcdefghijklmnopjrstuvwxyz0123456789";

    /**
     * 生成验证码
     */
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>>   ", e);
        }
    }
    
    
    
    /**
     * 忘记密码页面校验验证码
     */
    @PostMapping(value = "/checkVerify",headers = "Accept=application/json")
    public boolean checkVerify(@RequestBody Map<String, Object> requestMap, HttpSession session) {
        try{
            //从session中获取随机数
            String inputStr = requestMap.get("inputStr").toString();
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            logger.error("验证码校验失败", e);
            return false;
        }
    }
    
}
