package cc.stbl.token.innerdisc.restapi.app.user;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserImg;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfoAttribute;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserTrade;
import cc.stbl.token.innerdisc.modules.basic.service.ActivateFlowService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserImgService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoAttributeService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserTradeService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.user.VrUserProto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/user")
@Api(description = "用户模块")
public class UserRestController {

    public static Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private VrUserTradeService vrUserTradeService;

    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private EthWalletService walletService;

    @Autowired
    private VrUserImgService vrUserImgService;

    @Autowired
    private OssProperties ossProperties;
    
    @Autowired
    private ActivateFlowService activateFlowService;
    
    @Autowired
    private SmsVerifyService smsVerifyService;

	@Autowired
	private VrUserInfoAttributeService vrUserInfoAttributeService;

	@RequestMapping(value = { "/getUserAttribute" }, method = RequestMethod.POST)
	@ApiOperation("获取用户属性")
	public Response getUserInfoAttribute(@RequestBody @Valid VrUserProto.GetUserAttribute request) {
		VrUserInfoAttribute v = vrUserInfoAttributeService.getVrUserInfoAttribute(request.getUserId());
		return Response.success(v);
	}

	@RequestMapping(value = { "/saveUserAttribute" }, method = RequestMethod.POST)
	@ApiOperation("保存用户属性")
	public Response saveUserInfoAttribute(@RequestBody @Valid VrUserProto.UserAttribute request) {
		request.setUserId(ShiroUtils.getLoginUserId());
		VrUserInfoAttribute vrUserInfoAttribute = vrUserInfoAttributeService.selectByUserId(request.getUserId());
		if (vrUserInfoAttribute == null) {
			vrUserInfoAttribute = new VrUserInfoAttribute();
			BeanUtils.copyProperties(request, vrUserInfoAttribute);
			vrUserInfoAttribute.setId(JavaUUIDGenerator.get());
			vrUserInfoAttributeService.addVrUserInfoAttribute(vrUserInfoAttribute);
		} else {
			String oriId = vrUserInfoAttribute.getId();
			BeanUtils.copyProperties(request, vrUserInfoAttribute);
			vrUserInfoAttribute.setId(oriId);
			vrUserInfoAttributeService.updateVrUserInfoAttribute(vrUserInfoAttribute);
		}

		/*
		 * VrUserInfo vrUserInfo = vrUserInfoService.get(request.getUserId());
		 * vrUserInfo.setRemark(request.getRemark());
		 * vrUserInfoService.saveUserInfoAttribute(vrUserInfo, request);
		 */
		return Response.success("保存成功");
	}

    @RequestMapping(value = "/forgotPasswordFirstStep",method = RequestMethod.POST)
    @ApiOperation("忘记密码第一步")
    public Response forgotPasswordFirstStep(@RequestBody @Valid UserProto.RequestForgotPasswordFirstStep request){
  
    	String phoneNum = request.getPhoneNum();
        try{
        	
            //创建用户
        	VrUserInfo vrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
        	if(vrUserInfo == null) {
        		return Response.error("此用户不存在");
        	}
        	 //校验验证码
            smsVerifyService.verifySmsCode("0",request.getPhoneNum(),request.getSmsCode());
            return Response.success("ok");
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    
    
    @RequestMapping(value = {"/forgotPasswordSecondStep"},method = RequestMethod.POST)
    @ApiOperation("忘记密码第二步,重置重置密码")
    public Response forgotPasswordsecondStep(@RequestBody @Valid UserProto.RequestForgotPasswordSecondStep restPassword){
    	 try{
	    	String password1 = restPassword.getPassword1();
	    	String password2 = restPassword.getPassword2();
	    	if(!password1.equals(password2)) {
	    		return Response.error("输入的两次密码不一致");
	    	}
	        vrUserInfoService.updatePasswordBySms(restPassword.getPhoneNum(), password1);
	        return Response.success();
    	 }catch (Exception e){
             return Response.error(e.getMessage());
         }
    }
    
    @RequestMapping(value = "/threeUsers",method = RequestMethod.POST)
    @ApiOperation("获取3级子用户")
    public Response forgotPasswordFirstStep(@RequestBody @Valid UserProto.RequestThreeUsers request){
  
    	String phoneNum = request.getPhoneNum();
        try{
            //创建用户
        	VrUserInfo vrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
        	if(vrUserInfo == null) {
        		return Response.error("此用户不存在");
        	}else {
        		VrUserInfo dbVrUserInfo = vrUserInfoService.findDownLevelUsresByPhoneNum(phoneNum);
        		return Response.success(dbVrUserInfo);
        	}
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    
    
    @RequestMapping(value = "/activateRecord",method = RequestMethod.POST)
    @ApiOperation("激活记录接口")
    public Response activateRecord(){
  
    	String userId = ShiroUtils.getLoginUserId();
        try{
        	List<ActivateFlow> list = activateFlowService.findAllByUserId(userId);
        	return Response.success(list);
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = {"/deleteUser"},method = RequestMethod.POST)
    @ApiOperation("删除没激活用户")
    public Response deleteUser(@RequestBody @Valid UserProto.RequestUserDelete request){
    	VrUserInfo userInfo = vrUserInfoService.get(request.getUserId());
    	if(userInfo ==null) {
    		return Response.error("找不到这个用户");
    	}
    	
    	if(userInfo.getStatus() != 0) {
    		return Response.error("这个用户已经激活过了，不可以删除");
    	}
    	
    	vrUserInfoService.deleteUser(userInfo);
        return Response.success("删除成功");
    }
    
    
    @RequestMapping(value = {"/activateUser"},method = RequestMethod.POST)
    @ApiOperation("激活用户")
    public Response activateUser(@RequestBody @Valid UserProto.RequestActiveUser request){
    	
    	vrUserInfoService.activateUser(request.getPhoneNum(),request.getActiveMoney());
    	return Response.success("激活成功");
    	
    }
    
    
    
    
    @RequestMapping(value = {"/restPassword"},method = RequestMethod.POST)
    @ApiOperation("修改密码")
    public Response restPassword(@RequestBody @Valid UserProto.RequestRestPassword restPassword){
        vrUserInfoService.updatePassword(ShiroUtils.getLoginUserId(), restPassword.getOldPassword(), restPassword.getNewPassword());
        return Response.success("修改成功");
    }

    @RequestMapping(value = {"/modifyUserNickName"},method = RequestMethod.POST)
    @ApiOperation("用户昵称修改")
    public Response modifyUserNickName(@RequestBody @Valid UserProto.RequestModifyUserNickName requestModifyUserNickName){
        vrUserInfoService.updateUserNickName(ShiroUtils.getLoginUserId(), requestModifyUserNickName.getNickName());
        return Response.success("修改成功");
    }
    
    
    @RequestMapping(value = {"/getWalletAddress"},method = RequestMethod.GET)
    @ApiOperation("获取钱包")
    public Response getWalletAddress(){
        String address = vrUserInfoService.getWalletAddress();
        return Response.success(address);
    }

    @RequestMapping(value = "/modifyMobileCheck",method = RequestMethod.POST)
    @ApiOperation("更换手机号校验 天宇")
    public Response modifyMobileCheck(@RequestBody @Valid UserProto.RequestModifyMobileCheck request){
        vrUserInfoService.modifyMobileCheck(request.getMobile(), request.getPassword());
        return Response.success();
    }

    @RequestMapping(value = "/modifyMobile",method = RequestMethod.POST)
    @ApiOperation("更换手机号 天宇")
    public Response modifyMobile(@RequestBody @Valid UserProto.RequestModifyMobile request){
        vrUserInfoService.modifyMobile(ShiroUtils.getLoginUserId(), request.getMobile(), request.getSmsCode(), request.getNewPassword(), request.getCodeType());
        return Response.success();
    }

    @RequestMapping(value = {"/restPayPassword"},method = RequestMethod.POST)
    @ApiOperation("重置支付密码")
    public Response restPayPassword(@RequestBody @Valid UserProto.RequestRestPayPassword restPassword){
    	String smsCode = restPassword.getSmsCode();
    	String newPayPassword = restPassword.getNewPassword();
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo vrUserInfo = vrUserInfoService.get(userId);
    	smsVerifyService.verifySmsCode("0",vrUserInfo.getPhoneNum(),smsCode);
    	vrUserInfoService.updatePayPassword(vrUserInfo,newPayPassword);
        return Response.success("修改成功");
    }

    

    @RequestMapping(value = {"/transferMJ"},method = RequestMethod.POST)
    @ApiOperation("转出MJ")
    public Response transferMJ(@RequestBody @Valid UserProto.RequestTransferMJOrAIIC request){
    	
//    	if(1==1)return Response.error("关闭中，14点开放");
    	String phoneNum = request.getPhoneNum();
    	VrUserInfo vrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
    	if(vrUserInfo == null) {
    		return Response.error("此用户不存在");
    	}
    	String num = request.getNum();
    	int newNum = Integer.parseInt(num);
    	if(newNum == 0) {
    		return Response.error("数量不可以为 0");
    	}
    	
    	String payPassword = request.getPayPassword();
    	String oldPayPassword = payPassword;
    	payPassword = DESUtil.encryptString(payPassword);
    	
    	VrUserInfo fromUser = vrUserInfoService.get(ShiroUtils.getLoginUserId());
    	logger.info("payPassword:{}",payPassword);
    	logger.info("fromUser.getPayPassword():{}",fromUser.getPayPassword());
    	
    	logger.info(!payPassword.equals(fromUser.getPayPassword())+"======");
    	if(!payPassword.equals(fromUser.getPayPassword())) {
    		return Response.error("支付密码错误");
    	}
    	
    	vrUserTradeService.transferMJ(vrUserInfo,num,oldPayPassword);
        return Response.success("转出成功");
    }

    
    @RequestMapping(value = {"/transferList"},method = RequestMethod.POST)
    @ApiOperation("转账列表")
    @ResponseBody
    public Response<Pager<VrUserTrade>> list(@RequestBody @Valid VrUserTradeProto.ListVrUserTradeRequest request){
    	VrUserTrade query = new VrUserTrade();
        BeanUtils.copyProperties(request,query);
       // query.desc("updateTime");
        Pager<VrUserTrade> pager = vrUserTradeService.findPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager);
    }

    
    @RequestMapping(value = {"/transferAIIC"},method = RequestMethod.POST)
    @ApiOperation("转出AIIC")
    public Response transferAIIC(@RequestBody @Valid UserProto.RequestTransferMJOrAIIC request){
    	
//    	if(1==1)return Response.error("关闭中，14点开放");
    	String phoneNum = request.getPhoneNum();
    	VrUserInfo vrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
    	if(vrUserInfo == null) {
    		EthWallet ethWallet = walletService.get(phoneNum);//此字段有可能是钱包地址
    		if(ethWallet != null) {
    			String userId = ethWallet.getUserId();
    			vrUserInfo = vrUserInfoService.get(userId);
    			if(vrUserInfo == null) {
    				return Response.error("此用户不存在");
    			}
    		}else {
    			return Response.error("此用户不存在");
    		}
    	}
    	String num = request.getNum();
    	int newNum = Integer.parseInt(num);
    	if(newNum == 0) {
    		return Response.error("数量不可以为 0");
    	}
    	String payPassword = request.getPayPassword();
    	String oldPayPassword = payPassword;
    	payPassword = DESUtil.encryptString(payPassword);
    	VrUserInfo fromUser = vrUserInfoService.get(ShiroUtils.getLoginUserId());
    	if(!payPassword.equals(fromUser.getPayPassword())) {
    		return Response.error("支付密码错误");
    	}
    	
    	vrUserTradeService.transferAIIC(vrUserInfo,num,oldPayPassword);
        return Response.success("转出成功");
    }
    

    @RequestMapping(value = {"/transfer"},method = {RequestMethod.POST})
    @ApiOperation(value = "转出/转入")
    public Response transfer(@RequestBody @Valid UserProto.RequestTransfer requestTransfer){
        String loginUserId = ShiroUtils.getLoginUserId();
        VrUserInfo vrUserInfo = vrUserInfoService.get(loginUserId);
        VrUserInfo userByPhoneNum = vrUserInfoService.getUserByPhoneNum(requestTransfer.getAccount());
        if(userByPhoneNum == null) return Response.error("对方不存在");
        if(vrUserInfo.getUserId().equals(userByPhoneNum.getUserId())) return Response.error("不能给自己转账");
        vrTokenTradeService.transferFromExt(requestTransfer.getPayPassword(),vrUserInfo.getUserId(),userByPhoneNum.getUserId(),
                requestTransfer.getAssets(),"转出给" + userByPhoneNum.getUserName(),vrUserInfo.getUserName() + "转入"
        );

        //转出者消息
//        PushPayload pushPayload = PushPayloadUtil.buildPushObject_all_alias_alert(vrUserInfo.getPhoneNum(),"您的"+requestTransfer.getAssets()+"资产已经成功转给了"+userByPhoneNum.getPhoneNum());
//        PushResult result = pushService.push(pushPayload);
//        System.out.print("转出者推送测试结果：" + JSON.toJSONString(result));
        //转入者消息
//        PushPayload pushPayload2 = PushPayloadUtil.buildPushObject_all_alias_alert(userByPhoneNum.getPhoneNum(),"您已收到"+vrUserInfo.getPhoneNum()+"转给你的"+requestTransfer.getAssets()+"资产");
//        PushResult result2 = pushService.push(pushPayload2);
//        System.out.print("转入者推送测试结果：" + JSON.toJSONString(result2));
        return Response.success();
    }

    @RequestMapping(value = "/uploadUserIcon",method = {RequestMethod.POST})
    @ApiOperation(value = "用户头像路径保存  天宇")
    public Response uploadUserIcon(@RequestBody UserProto.RequestSaveImg requestSaveImg) {
        vrUserInfoService.saveUserIcon(requestSaveImg.getPath(), ShiroUtils.getLoginUserId());
        return Response.success();
    }

    @RequestMapping(value = "/uploadReceiptCodeImg",method = {RequestMethod.POST})
    @ApiOperation(value = "收款码图片路径保存 天宇")
    public Response uploadReceiptCodeImg(@RequestBody UserProto.RequestSaveImg requestSaveImg){
        vrUserImgService.saveReceiptCodeImg(requestSaveImg.getPath(), ShiroUtils.getLoginUserId(), requestSaveImg.getType());
        return Response.success();
    }

    @RequestMapping(value = "/myPromotionInfo",method = {RequestMethod.POST})
    @ApiOperation(value = "我的推广会员 天宇")
    public Response<UserProto.ResponseMyPromotion> myPromotionInfo(@RequestBody UserProto.RequestMyPromotion requestMyPromotion) {
        UserProto.ResponseMyPromotion myPromotionInfo = vrUserInfoService.getMyPromotionInfo(ShiroUtils.getLoginUserId(), requestMyPromotion);
        return Response.success(myPromotionInfo);
    }

    @RequestMapping(value = "/getReceiptCodes",method = {RequestMethod.GET})
    @ApiOperation(value = "收款码列表")
    public Response<List<UserProto.ReceiptCode>> getReceiptCodes(){
        String host = ossProperties.getDefault().getHost();
        List<UserProto.ReceiptCode> list = new ArrayList<>();

        /* 此段查询所有有效的码 */
        List<VrUserImg> vrUserImgs = vrUserImgService.getByUserId(ShiroUtils.getLoginUserId(), 1);
        vrUserImgs.stream().forEach(img ->{
            UserProto.ReceiptCode code = new UserProto.ReceiptCode();
            code.setType(img.getImgType());
            code.setUrl(host + img.getImgUrl());
            list.add(code);
        });

        /*VrUserImg wechatCode = vrUserImgService.getByUserId(ShiroUtils.getLoginUserId(), 1, UserImgTypeEnum.WECHAT.getCode());
        VrUserImg aliCode = vrUserImgService.getByUserId(ShiroUtils.getLoginUserId(), 1, UserImgTypeEnum.ALI_PAY.getCode());
        if (wechatCode != null){
            UserProto.ReceiptCode code = new UserProto.ReceiptCode();
            code.setType(wechatCode.getImgType());
            code.setUrl(host + wechatCode.getImgUrl());
            list.add(code);
        }
        if (aliCode != null){
            UserProto.ReceiptCode code2 = new UserProto.ReceiptCode();
            code2.setType(aliCode.getImgType());
            code2.setUrl(host + aliCode.getImgUrl());
            list.add(code2);
        }*/
        return Response.success(list);
    }

}
