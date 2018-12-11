/**
* generator by mybatis plugin Tue Jul 10 16:13:33 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.dao.EthTradeRecordMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.trades.EthTradeRecordProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class EthTradeRecordService extends DataStoreServiceImpl<String, EthTradeRecord, EthTradeRecordMapper> {

    @Autowired
    private VrUserInfoService userInfoService;
    @Autowired
    private VrTokenManager vrTokenManager;
    @Autowired
    private EthWalletService ethWalletService;
    @Autowired
    private TwdLinkedTradeService linkedTradeService;
    @Autowired
    private EthIntegralAmplifyService integralAmplifyService;

    public Pager<EthTradeRecord> findPager(EthTradeRecord query, Integer pageNo, Integer pageSize) {
        return super.findPage(query,pageNo,pageSize);
    }

    public Long findSumTradeAmount(EthTradeRecord query){
        return mapper.findSumTradeAmount(query);
    }

    public List<EthTradeRecord> unionAllUserID(EthTradeRecord ethTradeRecord){
        return mapper.unionAllUserID(ethTradeRecord);
    }

    public Pager<EthTradeRecordProto.ResponseGetTradeRecords> getPager(EthTradeRecordProto.RequestListCondition condition){
        EthTradeRecord query = new EthTradeRecord();
//        query.setFromUserId(condition.getUid());
//        query.setStartTradeDate(condition.getStartTradeDate());
//        query.setEntTradeDate(condition.getEntTradeDate());
//        query.setTradeNo(condition.getTradeNo());
//        query.setPhoneNum(condition.getPhoneNum());
        //query.setTradeStatus(2); // 1.交易申请中，2：交易成功

        Long total = mapper.getCount(query);
        Integer pageNo = condition.getPageNo();
        Integer pageSize = condition.getPageSize();
        Integer offset = (pageNo - 1) * pageSize;
        if (total == 0L) {
            return new Pager(pageNo, pageSize, new ArrayList(), total);
        } else {
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            List<EthTradeRecord> list = mapper.getList(query, rowBounds);
            List<EthTradeRecordProto.ResponseGetTradeRecords> records = new ArrayList<>();
            for (EthTradeRecord record : list){
                EthTradeRecordProto.ResponseGetTradeRecords tradeRecords = this.entity2ResponseGet(record);
                records.add(tradeRecords);
            }
            return new Pager(pageNo, pageSize, records, total);
        }
    }

    public List<EthTradeRecordProto.ResponseDetail> getDetail(String id){
        EthTradeRecord record = super.get(id);
        if (record == null){
            throw new StructWithCodeException(ResponseCode.TRADE_RECORD_NOT_EXIST);
        }
        Integer tradeType = record.getTradeType();
        if (BEnum.TRANSFER.type == tradeType){
            // 转账
            return this.getTransferDetail(record);
        }else if (BEnum.LINKED_SELL_TRANSFER.type == tradeType || BEnum.LINKED_SELL_TRANSFER.type == tradeType){
            // 买入卖出
            return this.getBuyAndSellDetail(record);
        }else if (TradeConsts.TRADE_TYPE_TRANSFER_AMPLIFY == tradeType){
            // 积分兑换
            return this.getIntegralDetail(record);
        }/*else if (TradeConsts.TRADE_TYPE_SYS_CHARGE == tradeType){
            // 充值
        }else if (TradeConsts.TRADE_TYPE_CONSUME == tradeType){
            // 消费
        }*/
        return null;
    }

    private List<EthTradeRecordProto.ResponseDetail> getBuyAndSellDetail(EthTradeRecord record){
        List<EthTradeRecordProto.ResponseDetail> list = new ArrayList<>();
        // 买入卖出
        EthTradeRecordProto.ResponseDetail buyer = this.getDetailBuyerSellerInfo(record.getFromUserId(), record.getTradeNo());
        EthTradeRecordProto.ResponseDetail seller = this.getDetailBuyerSellerInfo(record.getToUserId(), record.getTradeNo());
        list.add(buyer);
        list.add(seller);
        return list;
    }

    private List<EthTradeRecordProto.ResponseDetail> getTransferDetail(EthTradeRecord record){
        List<EthTradeRecordProto.ResponseDetail> list = new ArrayList<>();
        EthTradeRecordProto.ResponseDetail transferIn = this.getTransferDetailInOut(record, record.getFromUserId());
        transferIn.setFlowType(record.getFromFlowType());
        EthTradeRecordProto.ResponseDetail transferOut = this.getTransferDetailInOut(record, record.getToUserId());
        transferOut.setFlowType(record.getToFlowType());
        list.add(transferIn);
        list.add(transferOut);
        return list;
    }

    private EthTradeRecordProto.ResponseDetail getTransferDetailInOut(EthTradeRecord record, String userId){
        /* 买入的用户信息 */
        EthTradeRecordProto.ResponseDetail detail = new EthTradeRecordProto.ResponseDetail();
        // 用户信息
        VrUserInfo userInfo = userInfoService.get(record.getToUserId());
        detail.setAccount(userInfo.getPhoneNum());
        detail.setPhoneNum(userInfo.getPhoneNum());
        detail.setUserId(userInfo.getUserId());
        // 用户可用资产
        EthWallet wallet = ethWalletService.getUserWallet(record.getToUserId());
        BigInteger buyerBalanceOf = vrTokenManager.balanceOf(wallet.getAddress());
        detail.setAvailableAssets(buyerBalanceOf);
        // 用户受限资产
        BigInteger buyerLockedBalanceOf = vrTokenManager.lockedBalanceOf(wallet.getAddress());
        detail.setRestrictedAssets(buyerLockedBalanceOf);
        // 总积分
        BigInteger integral = vrTokenManager.integralOf(wallet.getAddress());
        detail.setIntegral(integral);
        detail.setAsset(record.getTradeAmount());
        return detail;
    }

    private List<EthTradeRecordProto.ResponseDetail> getIntegralDetail(EthTradeRecord record){
        /* 买入的用户信息 */
        EthTradeRecordProto.ResponseDetail detail = new EthTradeRecordProto.ResponseDetail();
        // 用户信息
        VrUserInfo userInfo = userInfoService.get(record.getToUserId());
        detail.setAccount(userInfo.getPhoneNum());
        detail.setPhoneNum(userInfo.getPhoneNum());
        detail.setUserId(userInfo.getUserId());
        // 用户可用资产
        EthWallet wallet = ethWalletService.getUserWallet(record.getToUserId());
        BigInteger buyerBalanceOf = vrTokenManager.balanceOf(wallet.getAddress());
        detail.setAvailableAssets(buyerBalanceOf);
        // 用户受限资产
        BigInteger buyerLockedBalanceOf = vrTokenManager.lockedBalanceOf(wallet.getAddress());
        detail.setRestrictedAssets(buyerLockedBalanceOf);
        // 总积分
        BigInteger integral = vrTokenManager.integralOf(wallet.getAddress());
        detail.setIntegral(integral);
        EthIntegralAmplify ethIntegralAmplify = integralAmplifyService.getByTransactionHash(record.getTransactionHash());
        detail.setAcquired_integral(ethIntegralAmplify.getReturnedIntegral());
        if (TradeConsts.FLOW_TYPE_BALANCE == ethIntegralAmplify.getTokenType()){
            // 1受限/2可用
            detail.setFlowType(2);
        }else {
            detail.setFlowType(1);
        }
        detail.setAsset(record.getTradeAmount());
        List<EthTradeRecordProto.ResponseDetail> list = new ArrayList<>();
        list.add(detail);
        return list;
    }

    private EthTradeRecordProto.ResponseDetail getDetailBuyerSellerInfo(String userId, String tradeNo){
        /* 买入的用户信息 */
        EthTradeRecordProto.ResponseDetail info = new EthTradeRecordProto.ResponseDetail();
        // 用户信息
        VrUserInfo userInfo = userInfoService.get(userId);
        info.setAccount(userInfo.getPhoneNum());
        info.setPhoneNum(userInfo.getPhoneNum());
        info.setUserId(userInfo.getUserId());
        // 用户可用资产
        EthWallet wallet = ethWalletService.getUserWallet(userId);
        BigInteger buyerBalanceOf = vrTokenManager.balanceOf(wallet.getAddress());
        info.setAvailableAssets(buyerBalanceOf);
        // 用户受限资产
        BigInteger buyerLockedBalanceOf = vrTokenManager.lockedBalanceOf(wallet.getAddress());
        info.setRestrictedAssets(buyerLockedBalanceOf);
        // 总积分
        BigInteger integral = vrTokenManager.integralOf(wallet.getAddress());
        info.setIntegral(integral);
        // 交易数据
        TwdLinkedTrade twdLinkedTrade = linkedTradeService.get(tradeNo);
        info.setAsset(new BigDecimal(twdLinkedTrade.getAssetCount()));
        info.setPrice(twdLinkedTrade.getSinglePrice());
        info.setAmount(twdLinkedTrade.getTradedCount());
        return info;
    }

    private EthTradeRecordProto.ResponseGetTradeRecords entity2ResponseGet(EthTradeRecord record){
        EthTradeRecordProto.ResponseGetTradeRecords tradeRecords = new EthTradeRecordProto.ResponseGetTradeRecords();
        BeanUtils.copyProperties(record, tradeRecords);
        String userId = record.getFromUserId();
        if (StringUtils.isEmpty(userId)){
            userId = record.getToUserId();
        }
        // 用户信息
        VrUserInfo userInfo = userInfoService.get(userId);
        tradeRecords.setAccount(userInfo.getPhoneNum());
        tradeRecords.setPhoneNum(userInfo.getPhoneNum());
        tradeRecords.setFromUserId(userInfo.getUserId());
        tradeRecords.setUserLevel(userInfo.getUserLevel());
        // 用户可用资产
        EthWallet wallet = ethWalletService.getUserWallet(userId);
        BigInteger buyerBalanceOf = vrTokenManager.balanceOf(wallet.getAddress());
        tradeRecords.setAvailableAssets(buyerBalanceOf);
        // 用户受限资产
        BigInteger buyerLockedBalanceOf = vrTokenManager.lockedBalanceOf(wallet.getAddress());
        tradeRecords.setRestrictedAssets(buyerLockedBalanceOf);
        // 总积分
        BigInteger integral = vrTokenManager.integralOf(wallet.getAddress());
        tradeRecords.setIntegral(integral);
        // 交易数据
        TwdLinkedTrade twdLinkedTrade = linkedTradeService.get(record.getTradeNo());
        if (twdLinkedTrade == null){
            return tradeRecords;
        }
        tradeRecords.setAsset(twdLinkedTrade.getAssetCount());
        tradeRecords.setPrice(twdLinkedTrade.getSinglePrice());
        tradeRecords.setAmount(twdLinkedTrade.getTradedCount());
        return tradeRecords;
    }

}