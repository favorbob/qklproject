/**
* generator by mybatis plugin Wed Aug 22 11:16:49 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.common.enums.UserImgTypeEnum;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserImg;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserImgService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.message.service.MessageAccepterService;
import cc.stbl.token.innerdisc.modules.trades.dao.TwdLinkedTradeRecordMapper;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeRecord;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import cc.stbl.token.innerdisc.restapi.app.message.MessageProto;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeProto;
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
public class TwdLinkedTradeRecordService extends DataStoreServiceImpl<String, TwdLinkedTradeRecord, TwdLinkedTradeRecordMapper> {

    @Autowired
    private MessageAccepterService messageAccepterService;
    @Autowired
    private VrUserImgService vrUserImgService;
    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private EthWalletService ethWalletService;
    @Autowired
    private OssProperties ossProperties;
    @Autowired
    private VrUserInfoService vrUserInfoService;
    @Autowired
    private TwdLinkedTradeService twdLinkedTradeService;

    public TwdLinkedTradeStatDay countTwdLinkedTradeStatDay(){
        return mapper.countTwdLinkedTradeStatDay();
    }

    public void sendTipMessage(String recordId) {
        MessageProto.MessageRequestSupports request = new MessageProto.MessageRequestSupports();
        TwdLinkedTradeRecord record = get(recordId);
        if(recordId == null || !record.getToUserId().equals(ShiroUtils.getLoginUserId())) throw new StructWithCodeException(ResponseCode.LINKED_TRADE_ORDER_NOT_FOUND);
        if(record.getTipStatus() == TradeConsts.LINKED_RECORD_TIP_STATUS_IS_TIP)return;
        record.setTipStatus(TradeConsts.LINKED_RECORD_TIP_STATUS_IS_TIP);
        update(record);
        request.setAccepterId(record.getFromUserId());
        request.setExtendId(recordId);
        messageAccepterService.sendMessage(request,record.getToUserId());
    }

    public Pager<TwdLinkedTradeRecord> findUserBuyAssetPage(TwdLinkedTradeRecord query, Integer pageNo, Integer pageSize) {
        Long total = mapper.findUserBuyAssetCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<TwdLinkedTradeRecord>(), total);
        }
        List<TwdLinkedTradeRecord> list = mapper.findUserBuyAssetList(query,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }

    public LinkedTradeProto.ResponseSellAssetPageData sellAssetPageData(String linkedId, String userId){
        LinkedTradeProto.ResponseSellAssetPageData data = new LinkedTradeProto.ResponseSellAssetPageData();
        if (StringUtils.isNotEmpty(linkedId)){
            TwdLinkedTrade twdLinkedTrade = twdLinkedTradeService.get(linkedId);
            if (twdLinkedTrade == null){
                throw new StructWithCodeException(ResponseCode.TRADE_RECORD_NOT_EXIST);
            }
            // 挂单信息
            data.setRecordId(linkedId);
            data.setSellerUserId(twdLinkedTrade.getUserId());
            data.setSoldAsset(new BigDecimal(twdLinkedTrade.getAssetCount()));
            data.setPrice(new BigDecimal(twdLinkedTrade.getSinglePrice()));
            data.setAmount(new BigDecimal(twdLinkedTrade.getTradedCount()));
            data.setDate(twdLinkedTrade.getCreateDate());
            data.setStatus(twdLinkedTrade.getLinkedStatus());
            data.setType(twdLinkedTrade.getLinkedType());
        }

        // 收款码列表
        List<VrUserImg> vrUserImgs = new ArrayList<>();
        VrUserImg wechatCode = vrUserImgService.getByUserId(userId, 1, UserImgTypeEnum.WECHAT.getCode());
        VrUserImg aliCode = vrUserImgService.getByUserId(userId, 1, UserImgTypeEnum.ALI_PAY.getCode());
        vrUserImgs.add(wechatCode);
        vrUserImgs.add(aliCode);
        List<LinkedTradeProto.VrUserImgProto> list = this.toVrUserImgProtos(vrUserImgs);
        data.setCodeList(list);

        // 我的资产
        EthWallet userWallet = ethWalletService.getUserWallet(userId);
        if(userWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        BigInteger balanceOf = vrTokenManager.balanceOf(userWallet.getAddress());
        data.setMyAsset(UnitConvertUtils.toEther(balanceOf));
        // 用户信息
        data.setUserId(userId);

        return data;
    }

    public LinkedTradeProto.ResponseBuyAssetPageData buyAssetPageData(String recordId, String userId){
        LinkedTradeProto.ResponseBuyAssetPageData data = new LinkedTradeProto.ResponseBuyAssetPageData();
        TwdLinkedTradeRecord tradeRecord = super.get(recordId);
        if (tradeRecord == null){
            throw new StructWithCodeException(ResponseCode.TRADE_RECORD_NOT_EXIST);
        }

        // 用户信息
        data.setUserId(userId);
        // 收款码列表
        List<VrUserImg> vrUserImgs = vrUserImgService.getByUserId(tradeRecord.getFromUserId(), 1);
        List<LinkedTradeProto.VrUserImgProto> list = this.toVrUserImgProtos(vrUserImgs);
        data.setCodeList(list);
        // 挂单信息
        data.setRecordId(recordId);
        data.setSellerUserId(tradeRecord.getFromUserId());
        data.setSoldAsset(new BigDecimal(tradeRecord.getAssetCount()));
        data.setPrice(new BigDecimal(tradeRecord.getSingleAmount()));
        data.setAmount(new BigDecimal(tradeRecord.getTotalAmount()));
        data.setDate(tradeRecord.getCreateDate());
        data.setStatus(tradeRecord.getLinkedType());
        data.setType(tradeRecord.getLinkedType());
        return data;
    }

    private List<LinkedTradeProto.VrUserImgProto> toVrUserImgProtos(List<VrUserImg> vrUserImgs){
        String host = ossProperties.getDefault().getHost();
        List<LinkedTradeProto.VrUserImgProto> list = new ArrayList<>();
        for (VrUserImg img : vrUserImgs){
            if (img != null){
                LinkedTradeProto.VrUserImgProto proto = new LinkedTradeProto.VrUserImgProto();
                BeanUtils.copyProperties(img, proto);
                proto.setImgUrl(host + img.getImgUrl());
                list.add(proto);
            }
        }
        return list;
    }

    public boolean hasProcessTradeRecord(String linkedId) {
        List<TwdLinkedTradeRecord> records = this.findByLinkedId(linkedId);
        if(records == null || records.size() == 0) return false;
        for (TwdLinkedTradeRecord record : records) {
            if(record.getTradeStatus() == TradeConsts.TRADE_STATUS_PROCESSING || record.getTradeStatus() == TradeConsts.TRADE_STATUS_SUCCESS) return true;
        }
        return false;
    }

    public List<TwdLinkedTradeRecord> findByLinkedId(String linkedId) {
        TwdLinkedTradeRecord query = new TwdLinkedTradeRecord();
        query.setLinkedId(linkedId);
        return findList(query);
    }

    public List<TwdLinkedTradeRecord> findUserLinkedList(TwdLinkedTradeRecord linkedTrade, Integer pageNo, Integer pageSize) {
        Integer offset = (pageNo - 1) * pageSize;
        List<TwdLinkedTradeRecord> twdLinkedTrades = this.mapper.findUserLinkedList(linkedTrade,new RowBounds(offset,pageSize));
        return twdLinkedTrades;
    }
}