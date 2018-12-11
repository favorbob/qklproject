package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthTradeRecordEventListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class LimitedTransferEvent extends AbstractVrTokenEvent{

    @Autowired
    private LimitedTransferEvent _this;


    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.limitedTransferEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(lockedTransferEventResponse -> _this.processEvent(lockedTransferEventResponse));
    }

    @Transactional
    public void processEvent(VRToken.LimitedTransferEventResponse event) {
        String tradeId = event.tradeNo;
        logger.info("LimitedTransferEventResponse={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
        EthTradeRecord up = tradeRecordService.get(tradeId);
        if(up == null) return;
        if(TradeConsts.TRADE_STATUS_SUCCESS.equals(up.getTradeStatus()) ) return;
        up.setId(tradeId);
        up.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        EthBlock.Block block = getBlock(event.log.getBlockNumber());
        up.setSuccessDate(block == null ? new Date() : new Date(block.getTimestamp().longValue() * 1000));
        up.setAtBlockNumber(event.log.getBlockNumber().longValue());
        up.setTransactionHash(event.log.getTransactionHash());
        tradeRecordService.update(up);
        BigDecimal limitedBalanceOfFrom= UnitConvertUtils.toEther(event.limitedBalanceOfFrom);
        BigDecimal limitedBalanceOfTo= UnitConvertUtils.toEther(event.limitedBalanceOfTo);
        recordFlow(up,limitedBalanceOfFrom,limitedBalanceOfTo);
        tradeRecordEventListener.sendTradeRecordEvent(up);
        setStartValue(up.getAtBlockNumber() - 1);
    }

    private void recordFlow(EthTradeRecord up, BigDecimal limitedBalanceOfFrom, BigDecimal limitedBalanceOfTo) {
        if (StringUtils.isNotEmpty(up.getFromUserId())) {
            ethAssetFlowService.createFlow(up.getTradeNo(),
                    up.getFromUserId(),
                    up.getTradeAmount(),
                    limitedBalanceOfFrom,
                    up.getFromFlowType(),
                    up.getTradeType(),
                    !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                    up.getTransactionHash(),
                    up.getAtBlockNumber(),
                    up.getFromRemark()
            );
        }
        if (StringUtils.isNotEmpty(up.getToUserId())) {
            ethAssetFlowService.createFlow(up.getTradeNo(),
                    up.getToUserId(),
                    up.getTradeAmount(),
                    limitedBalanceOfTo,
                    up.getToFlowType(),
                    up.getTradeType(),
                    TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                    up.getTransactionHash(),
                    up.getAtBlockNumber(),
                    up.getToRemark()
            );
        }
    }
}
