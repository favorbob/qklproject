package cc.stbl.token.innerdisc.restapi.app.trades;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeRecordService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/linked/tradeRecord"})
@Api(description = "挂单交易记录@Leon")
public class LinkedTradeRecordRestController {

    @Autowired
    private TwdLinkedTradeRecordService linkedTradeRecordService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @RequestMapping(value = {"/tipPayed"},method = RequestMethod.POST)
    public Response tipPayed(@RequestBody @Valid LinkedTradeRecordProto.RequestTipPayed request){
        linkedTradeRecordService.sendTipMessage(request.getRecordId());
        return Response.success();
    }

    @RequestMapping(value = {"/recordDetail"},method = RequestMethod.POST)
    public Response<LinkedTradeRecordProto.ResponseRecordDetail> recordDetail(@RequestBody @Valid LinkedTradeRecordProto.RequestRecordDetail request){
        TwdLinkedTradeRecord tradeRecord = linkedTradeRecordService.get(request.getRecordId());
        if(tradeRecord == null) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_NOT_FOUND);
        LinkedTradeRecordProto.ResponseRecordDetail detail = new LinkedTradeRecordProto.ResponseRecordDetail();
        BeanUtils.copyProperties(tradeRecord,detail);
        detail.setRecordId(tradeRecord.getId());
        return Response.success(detail);
    }

    @RequestMapping(value = {"/userRecord"},method = {RequestMethod.POST})
    @ApiOperation("用户买入/卖出记录")
    public Response<List<LinkedTradeRecordProto.ResponseUserRecord>> userRecord(@RequestBody @Valid LinkedTradeRecordProto.RequestUserRecord request){
        List<LinkedTradeRecordProto.ResponseUserRecord> result = new ArrayList<>();
        String userId = ShiroUtils.getLoginUserId();
        TwdLinkedTradeRecord record = new TwdLinkedTradeRecord();
        boolean isBuyRequest = request.getType() == TradeConsts.LINKED_TYPE_BUY;
        if(isBuyRequest) {
            record.setToUserId(userId);
        } else {
            record.setFromUserId(userId);
        }
        record.desc("createDate");
        record.setLinkedId(request.getLinkedId());
        record.setBalanceType(request.getBalanceType());
        List<TwdLinkedTradeRecord> pageList = linkedTradeRecordService.findUserLinkedList(record,request.getPageNo(),request.getPageSize());
//        VrUserInfo userInfo = vrUserInfoService.getLoginUser();
        for (TwdLinkedTradeRecord tradeRecord : pageList) {
            LinkedTradeRecordProto.ResponseUserRecord mock = new LinkedTradeRecordProto.ResponseUserRecord();
            String tgUser = isBuyRequest ? tradeRecord.getFromUserId() : tradeRecord.getToUserId();
            VrUserInfo info = vrUserInfoService.getUserUseCache(tgUser);
            mock.setDate(tradeRecord.getCreateDate());
            mock.setName(info.getUserName());
            mock.setRecordId(tradeRecord.getId());
            mock.setUserId(tgUser);
            mock.setContent(new StringBuilder(!isBuyRequest ? "卖出" : "买入").append(info.getUserName()).append(" ")
                    .append(!isBuyRequest?"-":"+").append(tradeRecord.getAssetCount()).append("资产")
                    .append("/").append(!isBuyRequest?"+":"-").append(AmountUtils.convertYuan(tradeRecord.getTotalAmount()))
                    .toString());
            mock.setSinglePrice(AmountUtils.convertYuan(tradeRecord.getSingleAmount()));
            mock.setLinkedId(tradeRecord.getLinkedId());
            mock.setAmount(tradeRecord.getAssetCount());
            mock.setLinkedType(tradeRecord.getLinkedType());
            mock.setTipStatus(tradeRecord.getTipStatus());
            mock.setTradeStatus(tradeRecord.getTradeStatus());
            mock.setAccepted(tradeRecord.getAccepted());
            mock.setBalanceType(tradeRecord.getBalanceType());
            mock.setBlockNumber(tradeRecord.getBlockNumber());
            mock.setTransactionHash(tradeRecord.getTransactionHash());
            mock.setSellerPhoneNum(tradeRecord.getSellerPhoneNum());
            result.add(mock);
        }
        return Response.success(result);
    }
    @RequestMapping(value = {"/toUserRecord"},method = {RequestMethod.POST})
    @ApiOperation("向用户买入/卖出记录")
    public Response<List<LinkedTradeRecordProto.ResponseToUserRecord>> toUserRecord(@RequestBody @Valid LinkedTradeRecordProto.RequestToUserRecord request){
        List<LinkedTradeRecordProto.ResponseToUserRecord> result = new ArrayList<>();
        String userId = ShiroUtils.getLoginUserId();
        TwdLinkedTradeRecord record = new TwdLinkedTradeRecord();
        if(request.getType() == TradeConsts.LINKED_TYPE_BUY) { //查买入
            record.setToUserId(userId);
            record.setLinkedType(TradeConsts.LINKED_TYPE_SELL);
        } else {
            record.setFromUserId(userId);
            record.setLinkedType(TradeConsts.LINKED_TYPE_BUY);
        }
        record.desc("createDate");
        List<TwdLinkedTradeRecord> pageList = linkedTradeRecordService.findPageList(record,request.getPageNo(),request.getPageSize());
        for (TwdLinkedTradeRecord tradeRecord : pageList) {
            LinkedTradeRecordProto.ResponseToUserRecord mock = new LinkedTradeRecordProto.ResponseToUserRecord();
            String tgUser = request.getType() == TradeConsts.LINKED_TYPE_BUY ? tradeRecord.getFromUserId() : tradeRecord.getToUserId();
            VrUserInfo info = vrUserInfoService.getUserUseCache(tgUser);
            mock.setDate(tradeRecord.getCreateDate());
            mock.setName(info.getUserName());
            mock.setLinkedId(tradeRecord.getLinkedId());
            mock.setRecordId(tradeRecord.getId());
            mock.setSinglePrice(AmountUtils.convertYuan(tradeRecord.getSingleAmount()));
            mock.setUserId(tgUser);
            mock.setAmount(tradeRecord.getAssetCount());
            mock.setLinkedType(tradeRecord.getLinkedType());
            mock.setAccepted(tradeRecord.getAccepted());
            mock.setTipStatus(tradeRecord.getTipStatus());
            mock.setTradeStatus(tradeRecord.getTradeStatus());
            result.add(mock);
        }
        return Response.success(result);
    }
}
