package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthTradeRecordEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class TradePlatformTransferEvent extends AbstractVrTokenEvent {

    @Autowired
    private TradePlatformTransferEvent _this;

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthAssetFlowService assetFlowService;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.tradePlatformTransferEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(tradePlatformTransferEventResponse -> _this.processEvent(tradePlatformTransferEventResponse));
    }

    @Transactional
    public void processEvent(VRToken.TradePlatformTransferEventResponse event){
        String tradeId = event.tradeNo;
        logger.info("TradePlatformTransferEvent_trade_record_id={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
        EthTradeRecord up = tradeRecordService.get(tradeId);
        if(up == null) return;
        if(up.getTradeStatus() == TradeConsts.TRADE_STATUS_SUCCESS ) return;
        up.setId(tradeId);
        up.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        EthBlock.Block block = getBlock(event.log.getBlockNumber());
        up.setSuccessDate(block == null ? new Date() : new Date(block.getTimestamp().longValue() * 1000));
        up.setAtBlockNumber(event.log.getBlockNumber().longValue());
        up.setTransactionHash(event.log.getTransactionHash());
        up.setTradeAmount(UnitConvertUtils.toEther(event.amount));
        tradeRecordService.update(up);
        createFlow(event, up);
        setStartValue(up.getAtBlockNumber() - 1);
        tradeRecordEventListener.sendTradeRecordEvent(up);
    }

    protected void createFlow(VRToken.TradePlatformTransferEventResponse event, EthTradeRecord up) {
        BigDecimal lockedBalanceOfFrom= UnitConvertUtils.toEther(event.lockedBalanceOfFrom);
        BigDecimal balanceOfTo= UnitConvertUtils.toEther(event.balanceOfTo);
        assetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                lockedBalanceOfFrom,
                up.getFromFlowType(),
                up.getTradeType(),
                !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getFromRemark()
        );
        assetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                balanceOfTo,
                up.getToFlowType(),
                up.getTradeType(),
                !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getToRemark()
        );
    }
}
