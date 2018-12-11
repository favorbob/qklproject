package cc.stbl.token.innerdisc.modules.eth.trades.listener.impl;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthIntegralAmplifyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class IntegralAmplifyForTradeRecordListener extends EthIntegralAmplifyListener {

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Override
    public void onSuccess(EthIntegralAmplify amplify) {
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(amplify.getLastBlockNumber());
        tradeRecord.setFromFlowType(amplify.getTokenType());
        tradeRecord.setToFlowType(amplify.getTokenType());
        tradeRecord.setFromRemark("积分兑换");
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(amplify.getUserId());
        tradeRecord.setFromAddress(amplify.getAddress());
        tradeRecord.setToUserId(amplify.getUserId());
        tradeRecord.setSuccessDate(new Date());
        tradeRecord.setAtBlockNumber(amplify.getAtBlockNumber());
        tradeRecord.setTransactionHash(amplify.getTransactionHash());
        tradeRecord.setToAddress(amplify.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(amplify.getOptType() == TradeConsts.OPT_TYPE_BY_USER ? BEnum.AMPLIFY.type : BEnum.AMPLIFY_SYS.type);
        tradeRecord.setTradeAmount(amplify.getTotalToken());
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        tradeRecord.setTradeNo(amplify.getId());
        tradeRecordService.add(tradeRecord);
    }
}
