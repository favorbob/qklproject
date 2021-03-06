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
public class UnLockedUserBalanceEvent extends AbstractVrTokenEvent{

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private UnLockedUserBalanceEvent unLockedTransferEvent;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.unLockedBalanceEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(event -> unLockedTransferEvent.processEvent(event));
    }

    @Transactional
    public void processEvent(VRToken.UnLockedBalanceEventResponse event) {
        String tradeId = event.tradeNo;
        logger.info("unLockedTransfer_trade_record_id={}`transactionHash={}",tradeId,event.log.getTransactionHash());
        EthTradeRecord up = tradeRecordService.get(tradeId);
        if(up.getTradeStatus() == TradeConsts.TRADE_STATUS_SUCCESS ) return;
        up.setId(tradeId);
        up.setTradeStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        EthBlock.Block block = getBlock(event.log.getBlockNumber());
        if(block != null) up.setSuccessDate(new Date(block.getTimestamp().longValue() * 1000));
        up.setAtBlockNumber(event.log.getBlockNumber().longValue());
        up.setTransactionHash(event.log.getTransactionHash());
        tradeRecordService.update(up);
        setStartValue(up.getAtBlockNumber() - 1);
        recordFlow(up,UnitConvertUtils.toEther(event.balanceOf),UnitConvertUtils.toEther(event.lockedBalanceOf));
        tradeRecordEventListener.sendTradeRecordEvent(up);
    }

    private void recordFlow(EthTradeRecord up,BigDecimal balanceOf,BigDecimal lockedBalanceOf){
        ethAssetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                balanceOf,
                TradeConsts.FLOW_TYPE_BALANCE,
                up.getTradeType(),
                TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getFromRemark()
        );
        ethAssetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                lockedBalanceOf,
                TradeConsts.FLOW_TYPE_LOCKED_BALANCE,
                up.getTradeType(),
                !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getFromRemark()
        );
    }
}
