package cc.stbl.token.innerdisc.restapi.admin.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeService;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeStatDayService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequestMapping(value = {PathPrefix.ADMIN_PATH + "/linked/trade"})
@RestController
@Api(description = "挂单平台@Leon")
public class LinkedTradeController {

    @Autowired
    private VrUserInfoService userInfoService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private EthWalletService ethWalletService;

    @Autowired
    private TwdLinkedTradeService twdLinkedTradeService;

    @Autowired
    private TwdLinkedTradeStatDayService twdLinkedTradeStatDayService;

    @Autowired
    private TwdLinkedTradeRecordService linkedTradeRecordService;

    @RequestMapping(value = "/navyUsers",method = RequestMethod.POST)
    @ApiOperation("系统用户")
    public Response<List<LinkedTradeProto.ResponseNavyUser>> navyUsers(){
        List<LinkedTradeProto.ResponseNavyUser> result = new ArrayList<>();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        VrUserInfo query = new VrUserInfo();
        query.setUserLevel(VrUserInfo.USER_LEVEL_SYS);
        List<VrUserInfo> userInfos = userInfoService.findList(query);
        for (VrUserInfo userInfo : userInfos) {
            LinkedTradeProto.ResponseNavyUser user = new LinkedTradeProto.ResponseNavyUser();
            EthWallet wallet = ethWalletService.getUserWallet(userInfo.getUserId());
            user.setAsset(UnitConvertUtils.toEther(vrTokenManager.balanceOf(wallet.getAddress(),vrToken)));
            user.setNickName(userInfo.getUserName());
            user.setNavyUserId(userInfo.getUserId());
            result.add(user);
        }
        return Response.success(result);
    }

    @RequestMapping(value = {"/confirmTrade"},method = {RequestMethod.POST})
    @ApiOperation(value = "确认收款",notes = "确认交易")
    public Response confirmTrade(@RequestBody @Valid LinkedTradeProto.RequestConfirmTrade request){
        twdLinkedTradeService.confirmSysTrade(request.getRecordId());
        return Response.success();
    }


    @RequestMapping(value = "/sysLinkedSeller",method = RequestMethod.POST)
    @ApiOperation("系统挂单")
    public Response sysLinkedSeller(@RequestBody @Valid LinkedTradeProto.RequestSysLinkedSeller request){
        twdLinkedTradeService.applySystemLinkedSell(request.getNavyUserId(),request.getTradeCount(),request.getTradeAmount().doubleValue());
        return Response.success("交易成功提交");
    }

    @RequestMapping(value = "/getStatisticalTradeAsset",method = {RequestMethod.GET})
    @ApiOperation(value = "获取挂单平台交易资产统计数据 天宇")
    public Response getStatisticalTradeAsset(){
        TwdLinkedTradeStatDay statisticalTradeAsset = twdLinkedTradeStatDayService.getStatisticalTradeAsset();
        return Response.success(statisticalTradeAsset);
    }

    @RequestMapping(value = "/userBuyAssetList",method = {RequestMethod.POST})
    @ApiOperation("用户向平台发起的购买列表")
    public Response<Pager<LinkedTradeProto.ResponseUserBuyAssetList>> userBuyAssetList(@RequestBody @Valid LinkedTradeProto.RequestUserBuyAssetList request){
        TwdLinkedTradeRecord query = new TwdLinkedTradeRecord();
        query.setTradeStatus(request.getStatus());
        query.setPhoneNum(request.getPhone());
        query.setStartDate(request.getStartDate());
        query.setEndDate(request.getEndDate());
        query.setSystemLinked(TradeConsts.OPT_TYPE_BY_SYSTEM);
        Pager<TwdLinkedTradeRecord> pager = linkedTradeRecordService.findUserBuyAssetPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager.reversalPager(rc -> {
            LinkedTradeProto.ResponseUserBuyAssetList by = new LinkedTradeProto.ResponseUserBuyAssetList();
            by.setStatus(rc.getAccepted());
            by.setTotalAmount(AmountUtils.convertYuan(rc.getTotalAmount()));
            by.setTradeAmount(AmountUtils.convertYuan(rc.getSingleAmount()));
            by.setTradeCount(rc.getAssetCount());
            by.setRecordId(rc.getId());
            by.setTradeDate(rc.getSuccessDate());
            by.setTipStatus(rc.getTipStatus());
            by.setUserId(rc.getFromUserId());
            by.setUserAccount(userInfoService.getUserUseCache(rc.getToUserId()).getPhoneNum());
            return Stream.of(by);
        }));
    }

    @RequestMapping(value = {"/tradeList"},method = {RequestMethod.POST})
    @ApiOperation("挂单列表")
    public Response<List<LinkedTradeProto.ResponseTradeInfo>> tradeList(@RequestBody @Valid LinkedTradeProto.RequestTradeList request){
        TwdLinkedTrade linkedTrade = new TwdLinkedTrade();
        linkedTrade.setCreateDate(request.getDate());
        linkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        if ("1".equals(request.getOrderMethod())){
            linkedTrade.asc("createDate");
        }else{
            linkedTrade.desc("createDate");
        }
        List<TwdLinkedTrade> list = twdLinkedTradeService.findPageList(linkedTrade,request.getPageNo(),request.getPageSize());
        List<LinkedTradeProto.ResponseTradeInfo> result = new ArrayList<>();
        for (TwdLinkedTrade trade : list) {
            LinkedTradeProto.ResponseTradeInfo mock = new LinkedTradeProto.ResponseTradeInfo();
            VrUserInfo userInfo = userInfoService.getUserUseCache(trade.getUserId());
            mock.setDate(trade.getCreateDate());
            mock.setAccount(userInfo.getPhoneNum());
            mock.setUserId(trade.getUserId());
            mock.setAmount(new BigDecimal(trade.getAssetCount()));
            mock.setPrice(Double.valueOf(AmountUtils.convertYuan(trade.getSinglePrice())));
            mock.setType(trade.getLinkedType());
            mock.setStatus(trade.getLinkedStatus());
            mock.setLinkedId(trade.getId());
            result.add(mock);
        }
        return Response.success(result);
    }

    /*@RequestMapping(value = "/setSalesPrice",method = {RequestMethod.POST})
    @ApiOperation(value = "设置平台出售价格")
    public Response setSalesPrice(@RequestBody @Valid LinkedTradeProto.RequestPrice request){
        twdLinkedTradeService.setCurrentSinglePrice(request.getPrice());
        return Response.success();
    }

    @RequestMapping(value = "/linkedOnState",method = {RequestMethod.GET})
    @ApiOperation(value = "平台开启状态")
    public Response<Boolean> linkedOnState(){
        return Response.success(twdLinkedTradeService.isOn());
    }

    @RequestMapping(value = "/setLinkedOnState",method = {RequestMethod.POST})
    @ApiOperation(value = "设置平台开启状态")
    public Response setLinkedOnState(@RequestBody @Valid LinkedTradeProto.RequestOnState request){
        twdLinkedTradeService.setOnOff(request.getOnState());
        return Response.success();
    }*/
}
