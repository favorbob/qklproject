package cc.stbl.token.innerdisc.restapi.app.payment;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.payment.service.MyAssetsService;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javaxx.financial.payment.channel.utils.AmountUtils;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/myAssets"})
@Api(description = "我的资产")
public class MyAssetsRestController {

    @Autowired
    private MyAssetsService myAssetsService;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private TwdLinkedTradeRecordService twdLinkedTradeRecordService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    //我的资产首页
    @RequestMapping(value = {"/myAssetsHome"},method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("我的资产首页")
    public Response<MyAssetsProto.ResponseMyAssetsSupports> myAssetsHome(){
        return myAssetsService.myAssetsHome();
    }

    //交易记录
    @RequestMapping(value = {"/transactionRecord"},method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("交易记录")
    public Response<List<MyAssetsProto.ResponseTradeInfo>> transactionRecord(){
        List<MyAssetsProto.ResponseTradeInfo> result =new ArrayList<>();
        String userId = ShiroUtils.getLoginUserId();
        List<EthTradeRecord> ethTradeRecords = myAssetsService.transactionRecord(userId);
        for (int i = 0;ethTradeRecords != null && i < ethTradeRecords.size(); i++) {
            EthTradeRecord ethTradeRecord =  ethTradeRecords.get(i);
            MyAssetsProto.ResponseTradeInfo responseTradeInfo = new MyAssetsProto.ResponseTradeInfo();
            responseTradeInfo.setId(ethTradeRecord.getId());
            responseTradeInfo.setTradeAmount(ethTradeRecord.getTradeAmount());
            responseTradeInfo.setTradeNo(ethTradeRecord.getTradeNo());
            responseTradeInfo.setBlockNumber(ethTradeRecord.getAtBlockNumber());
            responseTradeInfo.setTransactionHash(ethTradeRecord.getTransactionHash());
            if(ethTradeRecord.getFromUserId().equals(ethTradeRecord.getToUserId())) {
                responseTradeInfo.setBalance("+");
                if(ethTradeRecord.getTradeType() == TradeConsts.TRADE_TYPE_AMPLIFY){
                    responseTradeInfo.setBalance("-");
                }
            } else if(ethTradeRecord.getFromUserId().equals(userId)){
                responseTradeInfo.setBalance("-");
            }else if(ethTradeRecord.getToUserId().equals(userId)){
                responseTradeInfo.setBalance("+");
            }
            result.add(responseTradeInfo);
        }
        return Response.success(result);
    }

    //交易记录
    @RequestMapping(value = {"/transferRecord"},method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("转出记录")
    public Response<List<MyAssetsProto.ResponseTradeInfo>> transferRecord(@RequestBody @Valid MyAssetsProto.RequestType request){
        List<MyAssetsProto.ResponseTradeInfo> result =new ArrayList<>();
        List<EthTradeRecord> ethTradeRecords = new ArrayList<>();
        String userId = ShiroUtils.getLoginUserId();
        if (request.getType() == 0){
            ethTradeRecords  = myAssetsService.turnInRecord(userId);
        }else if(request.getType() == 1){
            ethTradeRecords  = myAssetsService.turnOutRecord(userId);
        }
        for (int i = 0;ethTradeRecords != null && i < ethTradeRecords.size(); i++) {
            EthTradeRecord ethTradeRecord =  ethTradeRecords.get(i);
            MyAssetsProto.ResponseTradeInfo responseTradeInfo = new MyAssetsProto.ResponseTradeInfo();
            responseTradeInfo.setId(ethTradeRecord.getId());
            responseTradeInfo.setTradeAmount(ethTradeRecord.getTradeAmount());
            responseTradeInfo.setTradeNo(ethTradeRecord.getTradeNo());
            responseTradeInfo.setDate(ethTradeRecord.getCreateDate());
            responseTradeInfo.setRemark(request.getType() == 0 ? ethTradeRecord.getToRemark() : ethTradeRecord.getFromRemark());
            responseTradeInfo.setBlockNumber(ethTradeRecord.getAtBlockNumber());
            responseTradeInfo.setTransactionHash(ethTradeRecord.getTransactionHash());
            result.add(responseTradeInfo);
        }
        return Response.success(result);
    }


    //资产明细
    @RequestMapping(value = {"/myAssetsDetail"},method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("资产明细")
    public Response<List<MyAssetsProto.ResponseAssetInfo>> myAssetsDetail(@RequestParam(value = "balanceType",required = false)Integer balanceType){
        List<EthAssetFlow> ethAssetFlows = myAssetsService.myAssetsDetail(balanceType);
        List<MyAssetsProto.ResponseAssetInfo> result = new ArrayList<>();
        for (int i = 0;ethAssetFlows != null && i < ethAssetFlows.size(); i++) {
            EthAssetFlow ethAssetFlow =  ethAssetFlows.get(i);
            MyAssetsProto.ResponseAssetInfo responseAssetInfo = new MyAssetsProto.ResponseAssetInfo();
            responseAssetInfo.setId(ethAssetFlow.getId());
            if(balanceType == null)   responseAssetInfo.setRemark(ethAssetFlow.getRemark() + "(" + TradeConsts.getBalanceTypeName(ethAssetFlow.getTradeType())+  ")");
            else  responseAssetInfo.setRemark(ethAssetFlow.getRemark());
            responseAssetInfo.setCreateTime(ethAssetFlow.getCreateTime());
            responseAssetInfo.setTradeAmount(ethAssetFlow.getTradeAmount().setScale(4, RoundingMode.HALF_UP));
            responseAssetInfo.setBalance(ethAssetFlow.getIsPlus() ? "+" : "-");
            responseAssetInfo.setBlockNumber(ethAssetFlow.getAtBlockNumber());
            responseAssetInfo.setTransactionHash(ethAssetFlow.getTransactionHash());
            result.add(responseAssetInfo);
        }
        return Response.success(result);
    }

    //交易详情
    @RequestMapping(value = {"/transactionDetail"},method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("交易详情")
    public Response<MyAssetsProto.ResponseTradeDetail> transactionDetail(@RequestParam(value = "id")String id){
        EthTradeRecord ethTradeRecord = myAssetsService.transactionDetail(id);
        MyAssetsProto.ResponseTradeDetail result = new MyAssetsProto.ResponseTradeDetail();
        BeanUtils.copyProperties(ethTradeRecord,result);
        VrUserInfo vrUserInfo = vrUserInfoService.get(ethTradeRecord.getToUserId());
        result.setToUser(vrUserInfo.getPhoneNum());
        result.setBlockNumber(ethTradeRecord.getAtBlockNumber());
        result.setTransactionHash(ethTradeRecord.getTransactionHash());
        if(ethTradeRecord.getTradeType() == TradeConsts.TRADE_TYPE_LINKED_CONFIRM_TRADE){
            TwdLinkedTradeRecord twdLinkedTradeRecord = twdLinkedTradeRecordService.get(ethTradeRecord.getTradeNo());
            result.setProfitAmount(AmountUtils.convertYuan(twdLinkedTradeRecord.getTotalAmount()));
        }
        return Response.success(result);
    }


    @RequestMapping(value = {"/convertibility"},method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("兑换为积分")
    public Response convertibility(@RequestBody @Valid MyAssetsRestController.RequestExchange requestExchange){
        String loginUserId = ShiroUtils.getLoginUserId();
        myAssetsService.integralAmplifyOf(loginUserId,requestExchange.getPayPassword(),requestExchange.getBalance(),requestExchange.getTokenType());
        return Response.success();
    }

    //积分流水
    @RequestMapping(value = {"/integralFlow"},method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("积分流水")
    public Response<Pager<MyAssetsRestController.ResponseIntegralFlow>> integralFlow(MyAssetsRestController.RequestPage request){
        IntegralFlow integralFlow = new IntegralFlow();
        integralFlow.setUserId(ShiroUtils.getLoginUserId());
        integralFlow.desc("createTime");
        Pager<IntegralFlow> pager = integralFlowService.findPage(integralFlow, request.getPageNo(), request.getPageSize());

        Pager<MyAssetsRestController.ResponseIntegralFlow> flowPager = new Pager<>();
        List<ResponseIntegralFlow> newList = new ArrayList<ResponseIntegralFlow>();
        BeanUtils.copyProperties(pager, flowPager);
        List<IntegralFlow> list = pager.getList();
        for (int i = 0; i < list.size(); i++) {
            IntegralFlow flow =  list.get(i);
            ResponseIntegralFlow responseIntegralFlow = new ResponseIntegralFlow();
            BeanUtils.copyProperties(flow, responseIntegralFlow);
            responseIntegralFlow.setTradeIntegral(flow.getTradeIntegral().setScale(4, RoundingMode.HALF_UP));
            responseIntegralFlow.setBusinessName(BEnum.getNameByCode(flow.getBusinessType()));
            responseIntegralFlow.setBlockNumber(flow.getAtBlockNumber());
            responseIntegralFlow.setTransactionHash(flow.getTransactionHash());
            newList.add(responseIntegralFlow);
        }
        flowPager.setList(newList);
        return Response.success(flowPager);
    }


    @Data
    public static class ResponseIntegralFlow {
        @ApiModelProperty("创建时间")
        private Date createTime;
        @ApiModelProperty("交易类型，0收入，1支出")
        private Integer tradeType;
        @ApiModelProperty("积分金额")
        private BigDecimal tradeIntegral;
        @ApiModelProperty("业务类型")
        private String businessName;
        private Long blockNumber;
        private String transactionHash;
    }

    @Data
    public static class RequestPage {
        Integer pageNo = 1;
        Integer pageSize = 20;
    }




    @Data
    public static class RequestExchange{
        @ApiModelProperty("支付密码")
        @NotBlank(message = "请输入支付密码")
        private String payPassword;

        @ApiModelProperty("兑换资产数")
        @Min(1)
        private BigDecimal balance;

        @ApiModelProperty("资产类型 "+TradeConsts.FLOW_TYPE_BALANCE+"：可用资产 "+TradeConsts.FLOW_TYPE_LIMITED_BALANCE+"：受限资产")
        private Integer tokenType;
    }

    //资产区块信息
}
