package cc.stbl.token.innerdisc.restapi.admin.user;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.authc.PasswordEncoder;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCount;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfoAttribute;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCountService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoAttributeService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInviteCodeImageService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLRValueService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserRmdService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/vrUser")
@Api(description = "Vr用户管理")
public class VrUserController {

    private static Logger logger = LoggerFactory.getLogger(VrUserController.class);

    
    @Autowired
    private VrUserLRValueService vrUserLRValueService;
    
    @Autowired
    private VrUserInfoService userInfoService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EthWalletService ethWalletService;

    @Autowired
    private VrUserRmdService vrUserRmdService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private VrUserCountService vrUserCountService;

    @Autowired
    private SysPropertiesService sysPropertiesService;
    @Autowired
    private VrUserInfoAttributeService vrUserInfoAttributeService;
    
    @Autowired
    private VrUserInviteCodeImageService vrUserInviteCodeImageService;

    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("用户列表")
    @ResponseBody
    public Response<Pager<VrUserProto.ResponseUserInfo>> list(@RequestBody @Valid VrUserProto.ListVrUserInfoRequest request){
        VrUserInfo query = new VrUserInfo();
        BeanUtils.copyProperties(request,query);
//        if(LoginUser.UserType.VR == ShiroUtils.getLoginUserType()){
//            query.setParentUserId(ShiroUtils.getLoginUserId());
//        }
        VRToken vrToken = vrTokenManager.getLastVrToken();
        query.desc("createDate");
        Pager<VrUserInfo> pager = userInfoService.findPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager.reversalPager(u-> {
            VrUserProto.ResponseUserInfo userInfo = new VrUserProto.ResponseUserInfo();
            
            String userCode = u.getUserCode();
            if(userCode !=null) {
	            String area = userCode.substring(userCode.length()-1, userCode.length())+"区";
	            userInfo.setArea(area);
            }
           
           // String level = getLevel(u.getaArea(),u.getbArea());
           // userInfo.setLevel(level);
            
            //u.set
            BeanUtils.copyProperties(u,userInfo);
            EthWallet userWallet = ethWalletService.getUserWallet(u.getUserId());
            if (userWallet != null){
                userInfo.setAvailableAssets(UnitConvertUtils.toEther(vrTokenManager.balanceOf(userWallet.getAddress(),vrToken)).setScale(2, RoundingMode.HALF_UP).toString());
                userInfo.setTotalBalance(UnitConvertUtils.toEther(vrTokenManager.totalBalanceOf(userWallet.getAddress(),vrToken)).setScale(2, RoundingMode.HALF_UP).toString());
                userInfo.setLimitAssets(UnitConvertUtils.toEther(vrTokenManager.limitBalanceOf(userWallet.getAddress(),vrToken)).setScale(2, RoundingMode.HALF_UP).toString());
                userInfo.setFrozenAssets(UnitConvertUtils.toEther(vrTokenManager.lockedBalanceOf(userWallet.getAddress(),vrToken)).setScale(2, RoundingMode.HALF_UP).toString());
                userInfo.setIntegral(UnitConvertUtils.toEther(vrTokenManager.integralOf(userWallet.getAddress(),vrToken)).setScale(2, RoundingMode.HALF_UP).toString());
            }
            if(u.getParent() != null) {
                userInfo.setParentPhoneNum(u.getParent().getPhoneNum());
                userInfo.setParentUserId(u.getParent().getUserId());
                userInfo.setParentUserName(u.getParent().getUserName());
            }
            VrUserCount vrUserCount = vrUserCountService.get(u.getUserId());
            userInfo.setVipUserCount(vrUserCount == null ? 0 : vrUserCount.getUserLevel1() + vrUserCount.getUserLevel2() + vrUserCount.getUserLeveln());
           
            try {
            	String newPassword = userInfo.getNewPassword();
            	String payPassword = userInfo.getPayPassword();
				userInfo.setNewPassword(DESUtil.decryptString(newPassword));
				 userInfo.setPayPassword(DESUtil.decryptString(payPassword));
			} catch (Exception e) {
				e.printStackTrace();
			}
            return Stream.of(userInfo);
        }));
    }

    
    @RequestMapping(value = {"/create"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:create"})
    @ApiOperation("添加用户")
    public Response<String> createUser(@RequestBody @Valid VrUserProto.CreateUserRequest request){
        VrUserInfo sysUser = new VrUserInfo();
        BeanUtils.copyProperties(request,sysUser);
        try{
            userInfoService.createNewUser(sysUser,request.getParentInviteCode());
        } catch (Exception e){
            return Response.error(e.getMessage());
        }
        return Response.success(sysUser.getUserId());
    }
    @RequestMapping(value = {"/freezeUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:freeze"})
    @ApiOperation("冻结用户")
    @Transactional
    public Response freezeUser(@RequestBody @Valid VrUserProto.UserFreeze request){
        VrUserInfo userInfo = userInfoService.get(request.getUserId());
        if(userInfo == null) return Response.error("用户不存在");
        VrUserInfo up = new VrUserInfo();
        up.setUpdateDate(new Date());
        up.setUserId(userInfo.getUserId());
        up.setStatus(VrUserInfo.USER_STATUS_FREEZE);
        up.setFreeze(request.getFreeze());//冻结原因
        userInfoService.update(up);
        
    	//更新  VrUserLRValue表status字段状态
		VrUserLRValue dbVrUserLRValue = vrUserLRValueService.get(request.getUserId());
		VrUserLRValue vrUserLRValue = new VrUserLRValue();
		vrUserLRValue.setUserId(dbVrUserLRValue.getUserId());
		vrUserLRValue.setStatus(VrUserInfo.USER_STATUS_FREEZE);
		vrUserLRValueService.update(vrUserLRValue);
        
        return Response.success("冻结成功");
    }
    @RequestMapping(value = {"/unFreezeUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:unfreeze"})
    @ApiOperation("解锁用户")
    @Transactional
    public Response unFreezeUser(@RequestBody @Valid VrUserProto.UserUnfreeze request){
        VrUserInfo userInfo = userInfoService.get(request.getUserId());
        if(userInfo == null) return Response.error("用户不存在");
        VrUserInfo up = new VrUserInfo();
        up.setUpdateDate(new Date());
        up.setUserId(userInfo.getUserId());
        up.setStatus(VrUserInfo.USER_STATUS_NORMAL);
        up.setUnfreeze(request.getUnfreeze());
        userInfoService.update(up);
        
      //更新  VrUserLRValue表status字段状态
	  VrUserLRValue dbVrUserLRValue = vrUserLRValueService.get(request.getUserId());
	  VrUserLRValue vrUserLRValue = new VrUserLRValue();
	  vrUserLRValue.setUserId(dbVrUserLRValue.getUserId());
	  vrUserLRValue.setStatus(VrUserInfo.USER_STATUS_NORMAL);
	  vrUserLRValueService.update(vrUserLRValue);
       return Response.success("解冻成功");
    }
    
    
    @RequestMapping(value = {"/remark"},method = RequestMethod.POST)
    @ApiOperation("备注")
    public Response remark(@RequestBody @Valid VrUserProto.UserRemark request){
        VrUserInfo userInfo = userInfoService.get(request.getUserId());
        if(userInfo == null) return Response.error("用户不存在");
        VrUserInfo up = new VrUserInfo();
        up.setUpdateDate(new Date());
        up.setUserId(userInfo.getUserId());
        up.setRemark(request.getRemark());
        userInfoService.update(up);
        return Response.success("修改成功");
    }
    
    
    @RequestMapping(value = {"/getUserAttribute"},method = RequestMethod.POST)
    @ApiOperation("获取用户属性")
    public Response getUserInfoAttribute(@RequestBody @Valid VrUserProto.GetUserAttribute request){
        VrUserInfoAttribute v = vrUserInfoAttributeService.getVrUserInfoAttribute(request.getUserId());
        return Response.success(v);
    }
    
//    @RequestMapping(value = {"/updateUserAttribute"},method = RequestMethod.POST)
//    @ApiOperation("更新用户属性")
//    public Response updateUserInfoAttribute(@RequestBody @Valid VrUserProto.UserAttribute request){
//    	VrUserInfoAttribute vrUserInfoAttribute = new VrUserInfoAttribute();
//        BeanUtils.copyProperties(request,vrUserInfoAttribute);
//        vrUserInfoAttributeService.updateVrUserInfoAttribute(vrUserInfoAttribute);
//        return Response.success("修改成功");
//    }
    
    @RequestMapping(value = {"/saveUserAttribute"},method = RequestMethod.POST)
    @ApiOperation("保存用户属性")
    public Response saveUserInfoAttribute(@RequestBody @Valid VrUserProto.UserAttribute request){
        VrUserInfoAttribute vrUserInfoAttribute = vrUserInfoAttributeService.selectByUserId(request.getUserId());
        if(vrUserInfoAttribute == null) {
        	vrUserInfoAttribute = new VrUserInfoAttribute();
        	BeanUtils.copyProperties(request,vrUserInfoAttribute);
        	vrUserInfoAttribute.setId(JavaUUIDGenerator.get());
        	vrUserInfoAttributeService.addVrUserInfoAttribute(vrUserInfoAttribute);
        }else {
        	BeanUtils.copyProperties(request,vrUserInfoAttribute);
        	vrUserInfoAttributeService.updateVrUserInfoAttribute(vrUserInfoAttribute);
        }
        
        
        VrUserInfo vrUserInfo = userInfoService.get(request.getUserId());
        vrUserInfo.setRemark(request.getRemark());
        userInfoService.saveUserInfoAttribute(vrUserInfo,request);
        return Response.success("保存成功");
    }

    @RequestMapping(value = {"/deleteUser"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:delete"})
    @ApiOperation("删除用户")
    public Response deleteUser(@RequestBody @Valid VrUserProto.UserId request){
        VrUserInfo userInfo = userInfoService.get(request.getUserId());
        if(userInfo == null) return Response.error("用户不存在");
        userInfoService.deleteUser(userInfo);
        return Response.success("删除成功");
    }

    @RequestMapping(value = {"/getCurrentUser"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("查询用户信息")
    public Response<VrUserProto.ResponseUserInfo> getCurrentUser(){
        VrUserProto.ResponseUserInfo vrUser = new VrUserProto.ResponseUserInfo();
        String userId=ShiroUtils.getLoginUserId();
        VrUserInfo userInfo = userInfoService.get(userId);
        BeanUtils.copyProperties(userInfo,vrUser);
        if(userInfo == null) return Response.error("用户不存在");
        userInfo.setPassword(null);
        userInfo.setSalt(null);
        userInfoService.loaderWalletInfo(Arrays.asList(userInfo));
        return Response.success(vrUser);
    }

    @RequestMapping(value = {"/getUserDownlineDetails"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("查询下线详情@唐昱轩")
    public Response<VrUserProto.ResponseUserDownlineDetail> getUserDownlineDetails(@RequestBody @Valid VrUserProto.UserId request){
        VrUserProto.ResponseUserDownlineDetail responseUserDownlineDetail = null;
        try {
            VrUserInfo userInfo = userInfoService.get(request.getUserId());
            responseUserDownlineDetail = new VrUserProto.ResponseUserDownlineDetail();
            BeanUtils.copyProperties(userInfo,responseUserDownlineDetail);
            EthWallet ethWallet = ethWalletService.getUserWallet(request.getUserId());
            //受限资产
            BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(ethWallet.getAddress());
            BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf);
            responseUserDownlineDetail.setRestrictedAssets(limitBalanceOfDecimal);
            //可用资产
            BigInteger balanceOf = vrTokenManager.balanceOf(ethWallet.getAddress());
            BigDecimal balanceOfDecimal = UnitConvertUtils.toEther(balanceOf);
            responseUserDownlineDetail.setAvailableAssets(balanceOfDecimal);
            //我的积分
            BigInteger integralOf = vrTokenManager.integralOf(ethWallet.getAddress());
            BigDecimal integralOfDecimal = UnitConvertUtils.toEther(integralOf);
            responseUserDownlineDetail.setMyPoints(integralOfDecimal);
            responseUserDownlineDetail.setBlockChainAddress(ethWallet.getAddress());
            VrUserRmd vrUserRmd = new VrUserRmd();
            vrUserRmd.setUserId(request.getUserId());
            VrUserRmd userRmd = vrUserRmdService.findOne(vrUserRmd);
            responseUserDownlineDetail.setParentUserId(userRmd.getParentUserId());
            responseUserDownlineDetail.setRegisterDate(userRmd.getRegisterDate());
            //查询下级人数
            VrUserCount vrUserCount = vrUserCountService.get(request.getUserId());
            responseUserDownlineDetail.setLevel1(vrUserCount.getUserLevel1());
            responseUserDownlineDetail.setLevel2(vrUserCount.getUserLevel2());
            responseUserDownlineDetail.setInfiniteLevel(vrUserCount.getUserLeveln());
            return Response.success(responseUserDownlineDetail);
        } catch (BeansException e) {
            logger.error("查询用户下线异常:" + ExceptionUtils.getFullStackTrace(e));
            return Response.error("查询用户下线异常");
        }

    }

    @RequestMapping(value = {"/getUserAvailableAssetsFlow"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("用户可用资产流水")
    public Response<Pager<VrUserAssetProto.ResponseAsstFlow>> getUserAvailableAssetsFlow(@RequestBody @Valid VrUserProto.RequestUser request){
        EthAssetFlow ethAssetFlow = new EthAssetFlow();
        ethAssetFlow.setUserId(request.getUserId());
        Pager<EthAssetFlow> pager = ethAssetFlowService.findPager(ethAssetFlow, request.getPageNo(), request.getPageSize());

        Pager<VrUserAssetProto.ResponseAsstFlow> flowPager = new Pager<>();
        BeanUtils.copyProperties(pager, flowPager);
        flowPager.setList(pager.getList().stream().flatMap(flow -> {
            VrUserAssetProto.ResponseAsstFlow responseAsstFlow = new VrUserAssetProto.ResponseAsstFlow();
            BeanUtils.copyProperties(flow, responseAsstFlow);
            responseAsstFlow.setTradeTypeName(BEnum.getNameByCode(flow.getBusinessType()));
            return Stream.of(responseAsstFlow);
        }).collect(Collectors.toList()));

        return Response.success(flowPager);
    }

    @RequestMapping(value = {"/getUserLimitAssetsFlow"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("用户受限资产流水")
    public Response<Pager<VrUserAssetProto.ResponseAsstFlow>> getUserLimitAssetsFlow(@RequestBody @Valid VrUserProto.RequestUser request){
        EthAssetFlow ethAssetFlow = new EthAssetFlow();
        ethAssetFlow.setUserId(request.getUserId());
        ethAssetFlow.setTradeType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        Pager<EthAssetFlow> pager = ethAssetFlowService.findPager(ethAssetFlow, request.getPageNo(), request.getPageSize());

        Pager<VrUserAssetProto.ResponseAsstFlow> flowPager = new Pager<>();
        BeanUtils.copyProperties(pager, flowPager);
        flowPager.setList(pager.getList().stream().flatMap(flow -> {
            VrUserAssetProto.ResponseAsstFlow responseAsstFlow = new VrUserAssetProto.ResponseAsstFlow();
            BeanUtils.copyProperties(flow, responseAsstFlow);
            responseAsstFlow.setTradeTypeName(BEnum.getNameByCode(flow.getBusinessType()));
            return Stream.of(responseAsstFlow);
        }).collect(Collectors.toList()));

        return Response.success(flowPager);
    }

    @RequestMapping(value = {"/getUserLimitIntegralFlow"},method = RequestMethod.POST)
    @RequiresPermissions(value = {"sys:vruser:view"})
    @ApiOperation("用户积分流水")
    public Response<Pager<VrUserAssetProto.ResponseIntegralFlow>> getUserLimitIntegralFlow(@RequestBody @Valid VrUserProto.RequestUser request){
        IntegralFlow integralFlow = new IntegralFlow();
        integralFlow.setUserId(request.getUserId());
        Pager<IntegralFlow> pager = integralFlowService.findPage(integralFlow, request.getPageNo(), request.getPageSize());

        Pager<VrUserAssetProto.ResponseIntegralFlow> flowPager = new Pager<>();
        BeanUtils.copyProperties(pager, flowPager);
        flowPager.setList(pager.getList().stream().flatMap(flow -> {
            VrUserAssetProto.ResponseIntegralFlow responseIntegralFlow = new VrUserAssetProto.ResponseIntegralFlow();
            BeanUtils.copyProperties(flow, responseIntegralFlow);
            return Stream.of(responseIntegralFlow);
        }).collect(Collectors.toList()));

        return Response.success(flowPager);
    }

    @RequestMapping(value = {"/batchUpdateUserInviteCodeQrCode"},method = RequestMethod.POST)
    @ApiOperation("批量更新用户邀请二维码")
    public Response batchUpdateUserInviteCodeQrCode(){
        List<VrUserInfo> list = userInfoService.findList(new VrUserInfo());
        for (VrUserInfo info : list){
            vrUserInviteCodeImageService.genInviteCodeImage(info.getUserId(), info.getInviteCode());
        }
        return Response.success();
    }

}
