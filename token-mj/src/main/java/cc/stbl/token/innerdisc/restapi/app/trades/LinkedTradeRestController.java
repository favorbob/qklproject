package cc.stbl.token.innerdisc.restapi.app.trades;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.wallet.EthWalletUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeService;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeStatDayService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.trades.CoinSettingProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/linked/trade"})
@Api(description = "挂单交易@Leon")
public class LinkedTradeRestController  {

    @Autowired
    private TwdLinkedTradeService linkedTradeService;

    @Autowired
    private TwdLinkedTradeRecordService twdLinkedTradeRecordService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private EthWalletService walletService;

    @Autowired
    private SysPropertiesService proServive;

    @RequestMapping(value = {"/currentPrice"},method = {RequestMethod.POST})
    @ApiOperation("今日单价")
    public Response currentPrice(){
        return Response.success(linkedTradeService.getCurrentSinglePrice());
    }

    @RequestMapping(value = "/applyLinkedSeller",method = {RequestMethod.POST})
    @ApiOperation(value = "申请在挂单平台卖币",notes = "用户将资产挂靠到挂单平台进行卖出交易,1:调用/api/fileTransfer/upload 上传收钱码, 2:冻结用户资产，3：生成挂单记录")
    public Response applyLinkedSeller(@RequestBody @Valid LinkedTradeProto.RequestApplyLinkedSeller request){
        Long aiicSellMinNum = new Long(proServive.getStringFromDB("prize", "aiic.sell.minNum"));//aiic卖出最小数量
        Long aiicSellMaxNum = new Long(proServive.getStringFromDB("prize", "aiic.sell.maxNum"));//aiic卖出最大数量
        Long mjSellMinNum = new Long(proServive.getStringFromDB("prize", "mj.sell.minNum"));//mj卖出最小数量
        Long mjSellMaxNum = new Long(proServive.getStringFromDB("prize", "mj.sell.maxNum"));//mj卖出最大数量
        Integer sellCount = request.getSellCount();
        Integer balanceType = request.getBalanceType();
        if(balanceType != null && balanceType!=2 && balanceType!=4){
            return Response.error("balanceType的值错误");
        }
        if(balanceType == 2 && (sellCount < aiicSellMinNum || sellCount > aiicSellMaxNum)){//aiic
            return Response.error(String.format("数量必须在%s-%s范围内", aiicSellMinNum,aiicSellMaxNum));
        }
        if(balanceType == 4 && (sellCount < mjSellMinNum || sellCount > mjSellMaxNum)){//mj
            return Response.error(String.format("数量必须在%s-%s范围内", mjSellMinNum,mjSellMaxNum));
        }
        linkedTradeService.applyLinkedSell(request.getSellCount(),request.getSellAmount().doubleValue(),request.getBalanceType());
        return Response.success(request);
    }

    @RequestMapping(value = "/applyLinkedBuy",method = {RequestMethod.POST})
    @ApiOperation(value = "(求购)申请在挂单平台买币",notes = "用户将资产挂靠到挂单平台进行买入交易,1:用户支付买入资产所需要的金额，2：生成挂单记录(支付中，支付异步回调),3:返回支付渠道所需信息，app调起支付")
    public Response applyLinkedBuy(@RequestBody @Valid LinkedTradeProto.RequestApplyLinkedBuy request){
        Long aiicBuyMinNum = new Long(proServive.getStringFromDB("prize", "aiic.buy.minNum"));//aiic买入最小数量
        Long aiicBuyMaxNum = new Long(proServive.getStringFromDB("prize", "aiic.buy.maxNum"));//aiic买入最大数量
        Long mjBuyMinNum = new Long(proServive.getStringFromDB("prize", "mj.buy.minNum"));//mj买入最小数量
        Long mjBuyMaxNum = new Long(proServive.getStringFromDB("prize", "mj.buy.maxNum"));//mj买入最大数量
        Integer buyCount = request.getBuyCount();
        Integer balanceType = request.getBalanceType();
        if(balanceType != null && balanceType!=2 && balanceType!=4){
            return Response.error("balanceType的值错误");
        }
        if(balanceType == 2 && (buyCount < aiicBuyMinNum || buyCount > aiicBuyMaxNum)){//aiic
            return Response.error(String.format("数量必须在%s-%s范围内", aiicBuyMinNum,aiicBuyMaxNum));
        }
        if(balanceType == 4 && (buyCount < mjBuyMinNum || buyCount > mjBuyMaxNum)){//mj
            return Response.error(String.format("数量必须在%s-%s范围内", mjBuyMinNum,mjBuyMaxNum));
        }
        linkedTradeService.applyLinkedBuy(request.getBuyCount(),request.getBuyAmount().doubleValue(),request.getBalanceType());
        return Response.success(request);
    }

    @RequestMapping(value = {"/buyAsset"},method = {RequestMethod.POST})
    @ApiOperation(value = "(买入)在挂单平台买币",notes = "1:核销挂单记录数据，2。支付金额，3.返回支付渠道所需信息，4：异步通知，失败回滚")
    public Response<LinkedTradeProto.ResponseBuyAsset> buyAsset(@RequestBody @Valid LinkedTradeProto.RequestBuyAsset request){
        String recordId=linkedTradeService.buyAsset(request.getLinkedId(), request.getBuyCount(),request.getBalanceType());
        LinkedTradeProto.ResponseBuyAsset res=new LinkedTradeProto.ResponseBuyAsset();
        res.setRecordId(recordId);
        return Response.success(res);
    }
    @RequestMapping(value = {"/sellAsset"},method = {RequestMethod.POST})
    @ApiOperation(value = "卖出资产给(求购)",notes = "1:核销挂单记录数据,2.扣除用户资产，3：平台转账给卖家")
    public Response sellAsset(@RequestBody @Valid LinkedTradeProto.RequestSellAsset request){
        linkedTradeService.sellAsset(request.getPayPassword(), request.getLinkedId(), request.getBuyCount());
        return Response.success(request);
    }

    @RequestMapping(value = {"/confirmTrade"},method = {RequestMethod.POST})
    @ApiOperation(value = "确认交易",notes = "确认交易")
    public Response confirmTrade(@RequestBody @Valid LinkedTradeProto.RequestConfirmTrade request){
        EthWallet wallet = walletService.getUserWallet(ShiroUtils.getLoginUserId());
        if(wallet == null) return Response.bulid(ResponseCode.WALLET_NOT_EXISTS);
        try {
            EthWalletUtils.loadCredentials(request.getPayPassword(),wallet.getWalletFile());
        } catch (Exception e) {
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        linkedTradeService.confirmTrade(request.getRecordId());
        return Response.success("");
    }

    @RequestMapping(value = {"/buySysAsset"},method = {RequestMethod.POST})
    @ApiOperation(value = "向平台购买资产",notes = "2。支付金额，3.返回支付渠道所需信息，4：异步通知")
    public Response<LinkedTradeProto.ResponseBuySysAsset> buySysAsset(@RequestBody @Valid LinkedTradeProto.RequestBuySysAsset request){
        String recordId = linkedTradeService.buySysAsset(request.getBuyCount());
        LinkedTradeProto.ResponseBuySysAsset res=new LinkedTradeProto.ResponseBuySysAsset();
        res.setRecordId(recordId);
        return Response.success(res);
    }

    @RequestMapping(value = {"/cancelLinked"},method = {RequestMethod.POST})
    @ApiOperation(value = "撤销挂单")
    public Response cancelLinked(@RequestBody @Valid LinkedTradeProto.RequestCancelLinked request){
        linkedTradeService.cancelLinked(request.getLinkedId());
        return Response.success(request);
    }

    @RequestMapping(value = {"/summary"},method = {RequestMethod.POST})
    @ApiOperation(value = "交易汇总统计")
    public Response<LinkedTradeProto.ResponseSummary> summary(@RequestBody @Valid LinkedTradeProto.RequestSummary request){
        return Response.success(linkedTradeService.getSummary(request.getBalanceType()));
    }
    @RequestMapping(value = {"/marketInfo"},method = {RequestMethod.POST})
    @ApiOperation("行情")
    public Response<List<LinkedTradeProto.ResponseMaketInfo>> marketInfo(){
        BigDecimal aiicPrice = new BigDecimal(proServive.getStringFromDB("prize", "aiic.price"));//0点时价
        LinkedTradeProto.ResponseMaketInfo aiicResponseMaketInfo = new  LinkedTradeProto.ResponseMaketInfo();
        aiicResponseMaketInfo.setBalanceType(TradeConsts.FLOW_TYPE_BALANCE);
        LinkedTradeProto.ResponseSummary summary = linkedTradeService.getSummary(TradeConsts.FLOW_TYPE_BALANCE);
        BigDecimal todayAveragePrice = linkedTradeService.getTodayAveragePrice(TradeConsts.FLOW_TYPE_BALANCE);
        if (summary != null){
            aiicResponseMaketInfo.setVolume(summary.getTotalValue().multiply(new BigDecimal(10000)).longValue());
        }
        aiicResponseMaketInfo.setLastdayPrice(aiicPrice);
        if(todayAveragePrice == null){
            aiicResponseMaketInfo.setTodayAveragePrice(aiicPrice);
        }else {
            aiicResponseMaketInfo.setTodayAveragePrice(todayAveragePrice);
        }
        List<LinkedTradeProto.ResponseMaketInfo> result = new ArrayList<>();
        result.add(aiicResponseMaketInfo);
        return Response.success(result);
    }

    @RequestMapping(value = {"/cancelTrade"},method = {RequestMethod.POST})
    @ApiOperation(value = "取消交易",notes = "取消交易")
    public Response cancelTrade(@RequestBody @Valid LinkedTradeProto.RequestConfirmTrade request){
        linkedTradeService.cancelTrade(request.getRecordId());
        return Response.success();
    }

    @RequestMapping(value = {"/tradeList"},method = {RequestMethod.POST})
    @ApiOperation("挂单列表")
    public Response<List<LinkedTradeProto.ResponseTradeInfo>> tradeList(@RequestBody @Valid LinkedTradeProto.RequestTradeList request){
        SimpleDateFormat smt = new SimpleDateFormat(" yyyy-MM-dd");
        String startTime = smt.format(request.getStartDate() == null ? new Date() : request.getStartDate()) + " 00:00:00";
        String endTime = smt.format(request.getEndDate() == null ? new Date() : request.getEndDate()) + " 23:59:59";
        TwdLinkedTrade linkedTrade = new TwdLinkedTrade();
        linkedTrade.setTradedCount(-1);
        linkedTrade.setStartTime(startTime);
        linkedTrade.setEndTime(endTime);
        linkedTrade.setLinkedType(request.getType());
        linkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        linkedTrade.orderBy(request.getOrderBy(),request.getDesc());
        linkedTrade.setBalanceType(request.getBalanceType());
        List<TwdLinkedTrade> list = linkedTradeService.findUserLinkedList(linkedTrade,request.getPageNo(),request.getPageSize());
        List<LinkedTradeProto.ResponseTradeInfo> result = new ArrayList<>();
        String userId = ShiroUtils.getLoginUserId();
        for (TwdLinkedTrade trade : list) {
            LinkedTradeProto.ResponseTradeInfo mock = new LinkedTradeProto.ResponseTradeInfo();
            VrUserInfo userInfo = vrUserInfoService.getUserUseCache(trade.getUserId());
            mock.setDate(trade.getCreateDate());
            mock.setAccount(userInfo.getPhoneNum());
            mock.setUserId(trade.getUserId());
            mock.setAmount(new BigDecimal(trade.getTradedCount()));
            mock.setPrice(Double.valueOf(AmountUtils.convertYuan(trade.getSinglePrice())));
            mock.setType(trade.getLinkedType());
            mock.setStatus(trade.getLinkedStatus());
            mock.setLinkedId(trade.getId());
            mock.setSelf(trade.getUserId().equals(userId));
            mock.setHasRecord(trade.getTradeRecord() == null ? 0 : 1);
            mock.setBalanceType(trade.getBalanceType());
            if(trade.getEthTradeRecord() != null) {
                mock.setBlockNumber(trade.getEthTradeRecord().getAtBlockNumber());
            }
            if(trade.getTradeRecord() != null) {
                mock.setRecordId(trade.getTradeRecord().getId());
            }
            result.add(mock);
        }
        return Response.success(result);
    }

    @RequestMapping(value = {"/userTradeList"},method = {RequestMethod.POST})
    @ApiOperation("挂单列表-用户记录(挂单求购，挂单卖出)")
    public Response<List<LinkedTradeProto.ResponseUserTradeInfo>> userTradeList(@RequestBody @Valid LinkedTradeProto.RequestUserTradeList request){
        TwdLinkedTrade linkedTrade = new TwdLinkedTrade();
        linkedTrade.setCreateDate(new Date());
        linkedTrade.setUserId(ShiroUtils.getLoginUserId());
//        linkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        linkedTrade.setLinkedType(request.getType());
        linkedTrade.setBalanceType(request.getBalanceType());
        linkedTrade.desc("l.createDate");
        List<TwdLinkedTrade> list = linkedTradeService.findUserLinkedList(linkedTrade,request.getPageNo(),request.getPageSize());
        List<LinkedTradeProto.ResponseUserTradeInfo> result = new ArrayList<>();
        for (TwdLinkedTrade trade : list) {
            LinkedTradeProto.ResponseUserTradeInfo mock = new LinkedTradeProto.ResponseUserTradeInfo();
            VrUserInfo userInfo = vrUserInfoService.getUserUseCache(trade.getUserId());
            mock.setDate(trade.getCreateDate());
            mock.setAccount(userInfo.getPhoneNum());
            mock.setUserId(trade.getUserId());
            mock.setLinkedId(trade.getId());
            mock.setAmount(new BigDecimal(trade.getAssetCount()));
            mock.setPrice(Double.valueOf(AmountUtils.convertYuan(trade.getSinglePrice())));
            mock.setType(trade.getLinkedType());
            mock.setStatus(trade.getLinkedStatus());
            mock.setHasRecord(trade.getTradeRecord() == null ? 0 : 1);
            mock.setBalanceType(trade.getBalanceType());
            if(trade.getEthTradeRecord() != null) {
                mock.setBlockNumber(trade.getEthTradeRecord().getAtBlockNumber());
                mock.setTransactionHash(trade.getEthTradeRecord().getTransactionHash());

            }
            if(trade.getTradeRecord() != null) {
                mock.setRecordId(trade.getTradeRecord().getId());
                mock.setTradeAssetCount(trade.getTradeRecord().getAssetCount());
                mock.setAccepted(trade.getTradeRecord().getAccepted());
                mock.setTipStatus(trade.getTradeRecord().getTipStatus());
                mock.setTradeStatus(trade.getTradeRecord().getTradeStatus());
            }
            result.add(mock);
        }
        return Response.success(result);
    }

    @RequestMapping(value = {"/sellAssetPageData"},method = {RequestMethod.GET})
    @ApiOperation(value = "卖出页面的数据 天宇")
    public Response<LinkedTradeProto.ResponseSellAssetPageData> sellAssetPageData(@RequestParam(value = "recordId", required = false)String recordId){
        return Response.success(twdLinkedTradeRecordService.sellAssetPageData(recordId, ShiroUtils.getLoginUserId()));
    }

    @RequestMapping(value = {"/buyAssetPageData"},method = {RequestMethod.GET})
    @ApiOperation(value = "求购页面的数据 天宇")
    public Response<LinkedTradeProto.ResponseBuyAssetPageData> buyAssetPageData(@RequestParam(value = "recordId", required = false)String recordId){
        return Response.success(twdLinkedTradeRecordService.buyAssetPageData(recordId, ShiroUtils.getLoginUserId()));
    }

    @RequestMapping(value = {"/getSystemReceiptCode"},method = RequestMethod.GET)
    @ApiOperation("获取买入页面系统收款码")
    public Response<String> getSystemReceiptCode(){
        String systemReceiptCode = siteSettingService.getSystemReceiptCode();
        if (StringUtils.isEmpty(systemReceiptCode)){
            throw new StructWithCodeException(ResponseCode.UN_KNOWN);
        }
        return Response.success(systemReceiptCode);
    }
}
