/**
* generator by mybatis plugin Tue Aug 21 10:32:32 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.common.TradeNoGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.sms.client.ISmsClient;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.modules.message.enm.MessageEnm;
import cc.stbl.token.innerdisc.modules.message.service.MessagePushService;
import cc.stbl.token.innerdisc.modules.trades.dao.TwdLinkedTradeMapper;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.sms.SmsConsts;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import javaxx.financial.payment.channel.utils.AmountUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TwdLinkedTradeService extends DataStoreServiceImpl<String, TwdLinkedTrade, TwdLinkedTradeMapper> {

    @Autowired
    private TradeNoGenerator noGenerator;

    @Autowired
    private VrTokenTradeService tokenTradeService;

    @Autowired
    private TwdLinkedTradeRecordService twdLinkedTradeRecordService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private ISmsClient smsClient;

    public boolean isOn(){
        return siteSettingService.getLinkedOnOff();
    }

    public void setOnOff(boolean onOff){
        siteSettingService.setLinkedOnOff(onOff);
    }

    public BigDecimal getCurrentSinglePrice(){
        return siteSettingService.getSellerPrice();
    }

    public void setCurrentSinglePrice(BigDecimal price){
        siteSettingService.setSellerPrice(price);
    }

    /**
     * 申请卖出
     * @param assetCount
     * @param single
     */
    public void applyLinkedSell(Integer assetCount,Double single,Integer balanceType){
        if(!isOn()) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_IS_DOWN);
        String loginUserId = ShiroUtils.getLoginUserId();
        TwdLinkedTrade apply = new TwdLinkedTrade();
        apply.setId(noGenerator.get());
        apply.setCreateDate(new Date());
        apply.setLinkedStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        apply.setLinkedType(TradeConsts.LINKED_TYPE_SELL);
        apply.setUserId(loginUserId);
        apply.setAssetCount(assetCount);
        apply.setSinglePrice(AmountUtils.convertMinute(single));
        apply.setTradedCount(assetCount);
        apply.setSystemLinked(TradeConsts.OPT_TYPE_BY_USER);
        apply.setBalanceType(balanceType);
        add(apply);
        if(balanceType == TradeConsts.FLOW_TYPE_BALANCE){
            tokenTradeService.lockBalance(loginUserId, BEnum.LINKED_LOCK,new BigDecimal(apply.getAssetCount()),"挂单冻结",apply.getId());
        }else {
            tokenTradeService.lockMJBalance(loginUserId, BEnum.LINKED_LOCK,new BigDecimal(apply.getAssetCount()),"挂单冻结",apply.getId());
        }
    }

    /**
     * 系统挂单卖出
     */
    public void applySystemLinkedSell(String userId,Integer assetCount,Double single){
        if(!isOn()) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_IS_DOWN);
        TwdLinkedTrade apply = new TwdLinkedTrade();
        apply.setId(noGenerator.get());
        apply.setCreateDate(new Date());
        apply.setLinkedStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        apply.setLinkedType(TradeConsts.LINKED_TYPE_SELL);
        apply.setSystemLinked(TradeConsts.OPT_TYPE_BY_SYSTEM_USER);
        apply.setUserId(userId);
        apply.setAssetCount(assetCount);
        apply.setSinglePrice(AmountUtils.convertMinute(single));
        apply.setTradedCount(assetCount);
        add(apply);
        tokenTradeService.lockBalance(userId, BEnum.LINKED_LOCK,new BigDecimal(apply.getAssetCount()),"挂单冻结",apply.getId());
    }

    /**
     * 申请买入
      * @param assetCount
     * @param single
     */
    public void applyLinkedBuy(Integer assetCount, Double single,Integer balanceType){
        if(!isOn()) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_IS_DOWN);
        String loginUserId = ShiroUtils.getLoginUserId();
        TwdLinkedTrade apply = new TwdLinkedTrade();
        apply.setId(noGenerator.get());
        apply.setCreateDate(new Date());
        apply.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        apply.setLinkedType(TradeConsts.LINKED_TYPE_BUY);
        apply.setUserId(loginUserId);
        apply.setAssetCount(assetCount);
        apply.setSinglePrice(AmountUtils.convertMinute(single));
        apply.setTradedCount(assetCount);
        apply.setSystemLinked(TradeConsts.OPT_TYPE_BY_USER);
        apply.setBalanceType(balanceType);
        add(apply);
        Map<String,String> extras = new HashMap<>();
        extras.put("CMD",MessageEnm.APPLY_LINKED_BUY.getName());
        VrUserInfo userInfo = vrUserInfoService.getUserUseCache(loginUserId);
        messagePushService.buildPushObject_android_ios_alert(MessageEnm.APPLY_LINKED_BUY.getTitle(),MessageEnm.APPLY_LINKED_BUY.getContent(),userInfo.getPhoneNum(),extras);
    }

    //购买资产
    public String buyAsset(String linkedId,Integer buyCount,Integer balanceType){
        String buyUserId = ShiroUtils.getLoginUserId();
        TwdLinkedTrade trade = get(linkedId);
        if(!Objects.equals(trade.getLinkedStatus(), TradeConsts.TRADE_STATUS_SUCCESS)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        if(trade.getLinkedType() != TradeConsts.LINKED_TYPE_SELL) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        if(trade.getTradedCount() < buyCount)  throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_ASSET_LESS);
        if(trade.getUserId().equals(buyUserId)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_LIMITED_SELF);
        trade.setTradedCount(trade.getTradedCount() - buyCount);

        TwdLinkedTradeRecord record = new TwdLinkedTradeRecord();
        record.setAssetCount(buyCount);
        record.setCreateDate(new Date());
        record.setFromUserId(trade.getUserId());
        record.setId(noGenerator.get());
        record.setLinkedId(trade.getId());
        record.setLinkedType(trade.getLinkedType());
        record.setSingleAmount(trade.getSinglePrice());
        record.setToUserId(buyUserId);
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_APPLY);
        record.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        record.setTotalAmount(buyCount * trade.getSinglePrice());
        record.setSystemLinked(trade.getSystemLinked());
        record.setBalanceType(balanceType);
        Map<String,String> extras = new HashMap<>();
        extras.put("CMD",MessageEnm.BUY_ASSET.getName());
        if(balanceType == TradeConsts.FLOW_TYPE_BALANCE){
            tokenTradeService.lockedTransferFrom(BEnum.LINKED_BUY_TRANSFER,trade.getUserId(),buyUserId,
                    new BigDecimal(record.getAssetCount()),"卖出资产","买入资产",
                    record.getId());
        }else {
            tokenTradeService.tradePlatformTransferLimit(trade.getUserId(),buyUserId, new BigDecimal(record.getAssetCount()),BEnum.LINKED_BUY_TRANSFER,"卖出资产","买入资产", record.getId());
        }
        update(trade);
        twdLinkedTradeRecordService.add(record);
        VrUserInfo userInfo = vrUserInfoService.getUserUseCache(buyUserId);
        messagePushService.buildPushObject_android_ios_alert(MessageEnm.BUY_ASSET.getTitle(),MessageEnm.BUY_ASSET.getContent(),userInfo.getPhoneNum(),extras);
        return record.getId();
    }

    public void buyAssetSuccess(String recordId){
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        if(TradeConsts.TRADE_STATUS_SUCCESS.equals(record.getTradeStatus())) return;
        record.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        twdLinkedTradeRecordService.update(record);
    }

    public void sellAssetSuccess(String recordId){
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        if(TradeConsts.TRADE_STATUS_SUCCESS.equals(record.getTradeStatus())) return;
        record.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        twdLinkedTradeRecordService.update(record);
    }

    //卖出资产
    public void sellAsset(String payPassword,String linkedId,Integer sellCount){
        String sellUserId = ShiroUtils.getLoginUserId();
        TwdLinkedTrade trade = get(linkedId);
        if(!Objects.equals(trade.getLinkedStatus(), TradeConsts.TRADE_STATUS_SUCCESS)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        if(trade.getLinkedType() != TradeConsts.LINKED_TYPE_BUY) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        if(trade.getTradedCount() < sellCount)  throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_ASSET_LESS);
        if(trade.getUserId().equals(sellUserId)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_LIMITED_SELF);
        trade.setTradedCount(trade.getTradedCount() - sellCount);
        TwdLinkedTradeRecord record = new TwdLinkedTradeRecord();
        record.setAssetCount(sellCount);
        record.setCreateDate(new Date());
        record.setFromUserId(sellUserId);
        record.setId(noGenerator.get());
        record.setLinkedId(trade.getId());
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_APPLY);
        record.setLinkedType(trade.getLinkedType());
        record.setSingleAmount(trade.getSinglePrice());
        record.setToUserId(trade.getUserId());
        record.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        record.setTotalAmount(sellCount * trade.getSinglePrice());
        record.setSystemLinked(trade.getSystemLinked());
        tokenTradeService.tradePlatformTransfer(BEnum.LINKED_SELL_TRANSFER,
                payPassword,sellUserId,trade.getUserId(),
                new BigDecimal(sellCount),"卖出资产","挂单买入",
                record.getId());
        update(trade);
        Map<String,String> extras = new HashMap<>();
        extras.put("CMD",MessageEnm.SELL_ASSET.getName());
        twdLinkedTradeRecordService.add(record);
        VrUserInfo userInfo = vrUserInfoService.getUserUseCache(sellUserId);
        messagePushService.buildPushObject_android_ios_alert(MessageEnm.SELL_ASSET.getTitle(),MessageEnm.SELL_ASSET.getContent(),userInfo.getPhoneNum(),extras);
    }


    public String buySysAsset(Integer buyCount) {
        TwdLinkedTradeRecord record = new TwdLinkedTradeRecord();
        record.setAssetCount(buyCount);
        record.setCreateDate(new Date());
//        record.setFromUserId("");
        record.setId(noGenerator.get());
        record.setLinkedId("");
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_APPLY);
        record.setLinkedType(TradeConsts.LINKED_TYPE_SYSTEM);
        record.setSingleAmount(AmountUtils.convertMinute(getCurrentSinglePrice().doubleValue()));
        record.setToUserId(ShiroUtils.getLoginUserId());
        record.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        record.setSystemLinked(TradeConsts.OPT_TYPE_BY_SYSTEM);
        record.setSuccessDate(new Date());
        record.setTotalAmount(buyCount * record.getSingleAmount());
        twdLinkedTradeRecordService.add(record);
        return record.getId();
//        tokenTradeService.tradePlatformTransfer(BEnum.LINKED_SELL_TRANSFER,record.getToUserId(),new BigDecimal(buyCount),BEnum.LINKED_SELL_TRANSFER.remark,record.getId());
    }

    public void confirmSysTrade(String recordId){
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        if(record.getTradeStatus() != TradeConsts.TRADE_STATUS_SUCCESS || record.getAccepted() != TradeConsts.ACCEPTED_STATUS_APPLY) {
            throw new StructWithCodeException(ResponseCode.TRADE_RECORD_NOT_EXIST);
        }
        record.setTradeStatus(TradeConsts.TRADE_STATUS_COMPLETE);
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_ALLOW);
        twdLinkedTradeRecordService.update(record);
        tokenTradeService.chargeAsset(record.getToUserId(),BEnum.LINKED_SYS_CHARGE,new BigDecimal(record.getAssetCount()),BEnum.LINKED_SYS_CHARGE.remark);
    }

    //收款方确认交易
    public void confirmTrade(String recordId) {
        String userId = ShiroUtils.getLoginUserId();
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        if(record.getTradeStatus() != TradeConsts.TRADE_STATUS_SUCCESS) {
            throw new StructWithCodeException(ResponseCode.TRADE_RECORD_NOT_EXIST);
        }
        if(!record.getFromUserId().equals(userId)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_RECORD_NO_OPT_AUTH);
        logger.info("confirmTrade_userId={}`recordId={}`descr=从用户{}冻结账户中解冻资产{}",userId,record,record.getToUserId(),record.getAssetCount());
        tokenTradeService.unLockBalance(record.getToUserId(),BEnum.LINKED_CONFIRM_TRADE,new BigDecimal(record.getAssetCount()),"购买资产",recordId);
        TwdLinkedTrade twdLinkedTrade = get(record.getLinkedId());
        if(twdLinkedTrade != null) {
            Integer cd = twdLinkedTrade.getConfirmCount() == null ? 0 : twdLinkedTrade.getConfirmCount();
            twdLinkedTrade.setConfirmCount(cd + record.getAssetCount());
            if(twdLinkedTrade.getConfirmCount() >= twdLinkedTrade.getAssetCount()) twdLinkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_COMPLETE);
            this.update(twdLinkedTrade);
        }
        record.setTradeStatus(TradeConsts.TRADE_STATUS_COMPLETE);
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_ALLOW);
        twdLinkedTradeRecordService.update(record);
        //给买家发送通知
        HashMap<String,Object> params = new HashMap<>();
        params.put("orderNo",record.getLinkedId());
        String toUserId = record.getToUserId();
        VrUserInfo toUserInfo = vrUserInfoService.get(toUserId);
        smsClient.sendSms(toUserInfo.getPhoneNum(),SmsConsts.SELLER_CONFIRM_SMS,params);
    }

    //收款方取消交易
    public void cancelTrade(String recordId){
        String userId = ShiroUtils.getLoginUserId();
        logger.info("cancelTrade_userId={}`recordId={}",userId,recordId);
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        if(record.getTradeStatus() != TradeConsts.TRADE_STATUS_SUCCESS) {
            throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        }
        if(!record.getFromUserId().equals(userId) && !record.getToUserId().equals(userId)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_RECORD_NO_OPT_AUTH);
        if(record.getAccepted() != 0) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_RECORD_STATUS_HAS_PROCESS);
        TwdLinkedTrade linkedTrade = this.get(record.getLinkedId());
        //求购卖方取消
        if(record.getFromUserId().equals(userId) && record.getLinkedType() == TradeConsts.LINKED_TYPE_BUY){
            logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=求购卖方取消,退至可用资产",userId,recordId,record.getLinkedType());
            if(record.getTipStatus() != 0) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_RECORD_CANNEL_BUYER_TIPS);
            tokenTradeService.tradePlatformTransferRefund(
                    record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),BEnum.LINKED_CANCEL_TRADE,
                    "求购卖方取消交易","卖出取消",
                    recordId);
        }
        //求购买方取消交易
        if(record.getToUserId().equals(userId) && record.getLinkedType() == TradeConsts.LINKED_TYPE_BUY) {
            logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=求购买方取消,退至可用资产",userId,recordId,record.getLinkedType());
            tokenTradeService.tradePlatformTransferRefund(
                    record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),
                    BEnum.LINKED_CANCEL_TRADE,
                    "买入取消","求购买方取消交易",
                    recordId);
        }
        //挂卖 --- 卖方取消交易
        if(record.getFromUserId().equals(userId)&& record.getLinkedType() == TradeConsts.LINKED_TYPE_SELL) {
            if(record.getTipStatus() != 0) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_RECORD_CANNEL_BUYER_TIPS);
            if(linkedTrade.getLinkedStatus() == TradeConsts.TRADE_TYPE_LINKED_CANCEL_LINKED) {
                logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=挂卖卖方取消交易,退至可用资产",userId,recordId,record.getLinkedType());
                tokenTradeService.tradePlatformTransferRefund(
                        record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),BEnum.LINKED_CANCEL_TRADE,
                        "挂卖方取消交易","卖出取消",
                        recordId);
            } else {
                logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=挂卖卖方取消交易,退至冻结资产",userId,recordId,record.getLinkedType());
                tokenTradeService.lockedTransferFrom(BEnum.LINKED_CANCEL_TRADE,
                        record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),
                        "挂卖方取消交易","卖出取消",
                        recordId);
            }
            //给买房发送短信通知
            HashMap<String,Object> params = new HashMap<>();
            params.put("orderNo",record.getLinkedId());
            String toUserId = record.getToUserId();
            VrUserInfo toUserInfo = vrUserInfoService.get(toUserId);
            smsClient.sendSms(toUserInfo.getPhoneNum(),SmsConsts.SELLER_CANCEL_SMS,params);
        }
        //挂卖 --- 买方取消交易
        if(record.getToUserId().equals(userId)&& record.getLinkedType() == TradeConsts.LINKED_TYPE_SELL) {
            if(linkedTrade.getLinkedStatus() == TradeConsts.TRADE_TYPE_LINKED_CANCEL_LINKED) {
                logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=挂卖买方取消交易,退至可用资产",userId,recordId,record.getLinkedType());
                tokenTradeService.tradePlatformTransferRefund(
                        record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),BEnum.LINKED_CANCEL_TRADE,
                        "买入取消","买方取消交易",
                        recordId);
            } else {
                logger.info("cancelTrade_userId={}`recordId={}`linkedType={}`descr=挂卖买方取消交易,退至冻结资产",userId,recordId,record.getLinkedType());
                tokenTradeService.lockedTransferFrom(BEnum.LINKED_CANCEL_TRADE,
                        record.getToUserId(),record.getFromUserId(),new BigDecimal(record.getAssetCount()),
                        "买入取消","买方取消交易",
                        recordId);
            }
            //给卖方方式短信通知
            HashMap<String,Object> params = new HashMap<>();
            params.put("orderNo",record.getLinkedId());
            String fromUserId = record.getFromUserId();
            VrUserInfo fromUserInfo = vrUserInfoService.get(fromUserId);
            smsClient.sendSms(fromUserInfo.getPhoneNum(),SmsConsts.BUYER_CANCEL_SMS,params);
        }

        if(linkedTrade != null && linkedTrade.getLinkedStatus() != TradeConsts.TRADE_STATUS_CANCEL) {
//            linkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_COMPLETE);
            linkedTrade.setTradedCount(linkedTrade.getTradedCount() + record.getAssetCount());
            this.update(linkedTrade);
        }
        record.setTradeStatus(TradeConsts.TRADE_STATUS_CANCEL);
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_REJECT);
        twdLinkedTradeRecordService.update(record);
    }

    //收款方取消交易
    public void cancelSysTrade(String recordId){
        TwdLinkedTradeRecord record = twdLinkedTradeRecordService.get(recordId);
        record.setTradeStatus(TradeConsts.TRADE_STATUS_COMPLETE);
        record.setAccepted(TradeConsts.ACCEPTED_STATUS_REJECT);
        twdLinkedTradeRecordService.update(record);
    }

    //取消挂单
    public void cancelLinked(String linkedId) {
        TwdLinkedTrade twdLinkedTrade = get(linkedId);
        if(twdLinkedTrade==null || !twdLinkedTrade.getUserId().equals(ShiroUtils.getLoginUserId())) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_NOT_FOUND);
        if(twdLinkedTrade.getLinkedStatus() != TradeConsts.TRADE_STATUS_SUCCESS) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_STATUS_ERROR);
        if(twdLinkedTradeRecordService.hasProcessTradeRecord(linkedId)) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_CANNEL_HAS_PROCESS_RECORD);

        if(twdLinkedTrade.getLinkedType() == TradeConsts.LINKED_TYPE_SELL) {
            BigDecimal unLockAmount = new BigDecimal(twdLinkedTrade.getTradedCount());
            tokenTradeService.unLockBalance(twdLinkedTrade.getUserId(),BEnum.LINKED_CANCEL_LINKED, unLockAmount,BEnum.LINKED_CANCEL_LINKED.remark,twdLinkedTrade.getId());
        }
        twdLinkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_CANCEL);
        Map<String,String> extras = new HashMap<>();
        extras.put("CMD",MessageEnm.CANCEL_ASSET.getName());
        this.update(twdLinkedTrade);
        String loginUserId = ShiroUtils.getLoginUserId();
        VrUserInfo userInfo = vrUserInfoService.getUserUseCache(loginUserId);
        messagePushService.buildPushObject_android_ios_alert(MessageEnm.CANCEL_ASSET.getTitle(),MessageEnm.CANCEL_ASSET.getContent(),userInfo.getPhoneNum(),extras);
    }

    public List<TwdLinkedTrade> findUserLinkedList(TwdLinkedTrade linkedTrade, Integer pageNo, Integer pageSize) {
        Integer offset = (pageNo - 1) * pageSize;
        List<TwdLinkedTrade> twdLinkedTrades = this.mapper.findUserLinkedList(linkedTrade,new RowBounds(offset,pageSize));
        return twdLinkedTrades;
    }
    public LinkedTradeProto.ResponseSummary getSummary(Integer balanceType) {
        HashMap summary = this.mapper.getSummary(balanceType);
        LinkedTradeProto.ResponseSummary  responseSummary = new LinkedTradeProto.ResponseSummary();
        if(summary != null){
            responseSummary.setHighPrice(new BigDecimal(String.valueOf(summary.get("highPrice"))));
            responseSummary.setLowPrice(new BigDecimal(String.valueOf(summary.get("lowPrice"))));
            responseSummary.setTotalValue(new BigDecimal(String.valueOf(summary.get("totalValue"))));
        }
        return responseSummary;
    }
    public BigDecimal getTodayAveragePrice(Integer balanceType) {
        BigDecimal todayAveragePrice = this.mapper.getTodayAveragePrice(balanceType);
        return todayAveragePrice;
    }
}