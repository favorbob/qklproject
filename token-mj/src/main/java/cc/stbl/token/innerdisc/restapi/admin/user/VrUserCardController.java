package cc.stbl.token.innerdisc.restapi.admin.user;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.ActivateCardGenerator;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCardService;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/vrUserCard")
@Api(description = "Vr卡管理")
public class VrUserCardController {

    private static Logger logger = LoggerFactory.getLogger(VrUserCardController.class);

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private VrUserCardService usercardService;

 
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    @ApiOperation("激活卡列表")
    @ResponseBody
    public Response<Pager<VrUserCardProto.ResponseUserCard>> list(@RequestBody @Valid VrUserCardProto.ListVrUserCardRequest request){
        VrUserCard query = new VrUserCard();
        BeanUtils.copyProperties(request,query);
        query.desc("updateTime");
        Pager<VrUserCard> pager = usercardService.findPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager.reversalPager(u-> {
        	VrUserCardProto.ResponseUserCard userCard = new VrUserCardProto.ResponseUserCard();
            BeanUtils.copyProperties(u,userCard);
            return Stream.of(userCard);
        }));
    }

    @RequestMapping(value = {"/create"},method = RequestMethod.POST)
    //@RequiresPermissions(value = {"sys:vruserCard:create"})
    @ApiOperation("添加卡")
    public Response<String> saveVrUserCard(@RequestBody @Valid VrUserCardProto.CreateUserCardRequest request){
    	
        try{
        	usercardService.saveVrUserCard(request.getPhoneNum(),ActivateCardGenerator.ActivateCardType.GS,request.getNum());
        } catch (Exception e){
            return Response.error(e.getMessage());
        }
        return Response.success();
    }
    
    @RequestMapping(value = {"/cardCountData"},method = RequestMethod.POST)
    //@RequiresPermissions(value = {"sys:vruserCard:create"})
    @ApiOperation("激活卡的统计数据")
    public Response getVrUserCardCount() {
    	try{
    		return Response.success(usercardService.getVrUserCardCount());
    	} catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
//    @RequestMapping(value = {"/freezeUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:freeze"})
//    @ApiOperation("冻结用户")
//    public Response freezeUser(@RequestBody @Valid VrUserProto.UserId request){
//        VrUserInfo userInfo = userInfoService.get(request.getUserId());
//        if(userInfo == null) return Response.error("用户不存在");
//        VrUserInfo up = new VrUserInfo();
//        up.setUpdateDate(new Date());
//        up.setUserId(userInfo.getUserId());
//        up.setStatus(VrUserInfo.USER_STATUS_FREEZE);
//        userInfoService.update(up);
//        return Response.success("冻结成功");
//    }
//    @RequestMapping(value = {"/unFreezeUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:unfreeze"})
//    @ApiOperation("解锁用户")
//    public Response unFreezeUser(@RequestBody @Valid VrUserProto.UserId request){
//        VrUserInfo userInfo = userInfoService.get(request.getUserId());
//        if(userInfo == null) return Response.error("用户不存在");
//        VrUserInfo up = new VrUserInfo();
//        up.setUpdateDate(new Date());
//        up.setUserId(userInfo.getUserId());
//        up.setStatus(VrUserInfo.USER_STATUS_NORMAL);
//        userInfoService.update(up);
//        return Response.success("解冻成功");
//    }
//
//    @RequestMapping(value = {"/deleteUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:delete"})
//    @ApiOperation("删除用户")
//    public Response deleteUser(@RequestBody @Valid VrUserProto.UserId request){
//        VrUserInfo userInfo = userInfoService.get(request.getUserId());
//        if(userInfo == null) return Response.error("用户不存在");
//        userInfoService.deleteUser(userInfo);
//        return Response.success("删除成功");
//    }
//
//    @RequestMapping(value = {"/getCurrentUser"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
//    @ApiOperation("查询用户信息")
//    public Response<VrUserProto.ResponseUserInfo> getCurrentUser(){
//        VrUserProto.ResponseUserInfo vrUser = new VrUserProto.ResponseUserInfo();
//        String userId=ShiroUtils.getLoginUserId();
//        VrUserInfo userInfo = userInfoService.get(userId);
//        BeanUtils.copyProperties(userInfo,vrUser);
//        if(userInfo == null) return Response.error("用户不存在");
//        userInfo.setPassword(null);
//        userInfo.setSalt(null);
//        userInfoService.loaderWalletInfo(Arrays.asList(userInfo));
//        return Response.success(vrUser);
//    }
//
//    @RequestMapping(value = {"/getUserDownlineDetails"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
//    @ApiOperation("查询下线详情@唐昱轩")
//    public Response<VrUserProto.ResponseUserDownlineDetail> getUserDownlineDetails(@RequestBody @Valid VrUserProto.UserId request){
//        VrUserProto.ResponseUserDownlineDetail responseUserDownlineDetail = null;
//        try {
//            VrUserInfo userInfo = userInfoService.get(request.getUserId());
//            responseUserDownlineDetail = new VrUserProto.ResponseUserDownlineDetail();
//            BeanUtils.copyProperties(userInfo,responseUserDownlineDetail);
//            EthWallet ethWallet = ethWalletService.getUserWallet(request.getUserId());
//            //受限资产
//            BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(ethWallet.getAddress());
//            BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf);
//            responseUserDownlineDetail.setRestrictedAssets(limitBalanceOfDecimal);
//            //可用资产
//            BigInteger balanceOf = vrTokenManager.balanceOf(ethWallet.getAddress());
//            BigDecimal balanceOfDecimal = UnitConvertUtils.toEther(balanceOf);
//            responseUserDownlineDetail.setAvailableAssets(balanceOfDecimal);
//            //我的积分
//            BigInteger integralOf = vrTokenManager.integralOf(ethWallet.getAddress());
//            BigDecimal integralOfDecimal = UnitConvertUtils.toEther(integralOf);
//            responseUserDownlineDetail.setMyPoints(integralOfDecimal);
//            responseUserDownlineDetail.setBlockChainAddress(ethWallet.getAddress());
//            VrUserRmd vrUserRmd = new VrUserRmd();
//            vrUserRmd.setUserId(request.getUserId());
//            VrUserRmd userRmd = vrUserRmdService.findOne(vrUserRmd);
//            responseUserDownlineDetail.setParentUserId(userRmd.getParentUserId());
//            responseUserDownlineDetail.setRegisterDate(userRmd.getRegisterDate());
//            //查询下级人数
//            VrUserCount vrUserCount = vrUserCountService.get(request.getUserId());
//            responseUserDownlineDetail.setLevel1(vrUserCount.getUserLevel1());
//            responseUserDownlineDetail.setLevel2(vrUserCount.getUserLevel2());
//            responseUserDownlineDetail.setInfiniteLevel(vrUserCount.getUserLeveln());
//            return Response.success(responseUserDownlineDetail);
//        } catch (BeansException e) {
//            logger.error("查询用户下线异常:" + ExceptionUtils.getFullStackTrace(e));
//            return Response.error("查询用户下线异常");
//        }
//
//    }
//
//    @RequestMapping(value = {"/getUserAvailableAssetsFlow"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
//    @ApiOperation("用户可用资产流水")
//    public Response<Pager<VrUserAssetProto.ResponseAsstFlow>> getUserAvailableAssetsFlow(@RequestBody @Valid VrUserProto.RequestUser request){
//        EthAssetFlow ethAssetFlow = new EthAssetFlow();
//        ethAssetFlow.setUserId(request.getUserId());
//        Pager<EthAssetFlow> pager = ethAssetFlowService.findPager(ethAssetFlow, request.getPageNo(), request.getPageSize());
//
//        Pager<VrUserAssetProto.ResponseAsstFlow> flowPager = new Pager<>();
//        BeanUtils.copyProperties(pager, flowPager);
//        flowPager.setList(pager.getList().stream().flatMap(flow -> {
//            VrUserAssetProto.ResponseAsstFlow responseAsstFlow = new VrUserAssetProto.ResponseAsstFlow();
//            BeanUtils.copyProperties(flow, responseAsstFlow);
//            responseAsstFlow.setTradeTypeName(BEnum.getNameByCode(flow.getBusinessType()));
//            return Stream.of(responseAsstFlow);
//        }).collect(Collectors.toList()));
//
//        return Response.success(flowPager);
//    }
//
//    @RequestMapping(value = {"/getUserLimitAssetsFlow"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
//    @ApiOperation("用户受限资产流水")
//    public Response<Pager<VrUserAssetProto.ResponseAsstFlow>> getUserLimitAssetsFlow(@RequestBody @Valid VrUserProto.RequestUser request){
//        EthAssetFlow ethAssetFlow = new EthAssetFlow();
//        ethAssetFlow.setUserId(request.getUserId());
//        ethAssetFlow.setTradeType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
//        Pager<EthAssetFlow> pager = ethAssetFlowService.findPager(ethAssetFlow, request.getPageNo(), request.getPageSize());
//
//        Pager<VrUserAssetProto.ResponseAsstFlow> flowPager = new Pager<>();
//        BeanUtils.copyProperties(pager, flowPager);
//        flowPager.setList(pager.getList().stream().flatMap(flow -> {
//            VrUserAssetProto.ResponseAsstFlow responseAsstFlow = new VrUserAssetProto.ResponseAsstFlow();
//            BeanUtils.copyProperties(flow, responseAsstFlow);
//            responseAsstFlow.setTradeTypeName(BEnum.getNameByCode(flow.getBusinessType()));
//            return Stream.of(responseAsstFlow);
//        }).collect(Collectors.toList()));
//
//        return Response.success(flowPager);
//    }
//
//    @RequestMapping(value = {"/getUserLimitIntegralFlow"},method = RequestMethod.POST)
//    @RequiresPermissions(value = {"sys:vruser:view"})
//    @ApiOperation("用户积分流水")
//    public Response<Pager<VrUserAssetProto.ResponseIntegralFlow>> getUserLimitIntegralFlow(@RequestBody @Valid VrUserProto.RequestUser request){
//        IntegralFlow integralFlow = new IntegralFlow();
//        integralFlow.setUserId(request.getUserId());
//        Pager<IntegralFlow> pager = integralFlowService.findPage(integralFlow, request.getPageNo(), request.getPageSize());
//
//        Pager<VrUserAssetProto.ResponseIntegralFlow> flowPager = new Pager<>();
//        BeanUtils.copyProperties(pager, flowPager);
//        flowPager.setList(pager.getList().stream().flatMap(flow -> {
//            VrUserAssetProto.ResponseIntegralFlow responseIntegralFlow = new VrUserAssetProto.ResponseIntegralFlow();
//            BeanUtils.copyProperties(flow, responseIntegralFlow);
//            return Stream.of(responseIntegralFlow);
//        }).collect(Collectors.toList()));
//
//        return Response.success(flowPager);
//    }
//
//    @RequestMapping(value = {"/batchUpdateUserInviteCodeQrCode"},method = RequestMethod.POST)
//    @ApiOperation("批量更新用户邀请二维码")
//    public Response batchUpdateUserInviteCodeQrCode(){
//        List<VrUserInfo> list = userInfoService.findList(new VrUserInfo());
//        for (VrUserInfo info : list){
//            vrUserInviteCodeImageService.genInviteCodeImage(info.getUserId(), info.getInviteCode());
//        }
//        return Response.success();
//    }

}
