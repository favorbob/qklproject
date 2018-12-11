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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class UnLockedBalanceToLimitEvent extends AbstractVrTokenEvent {


    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private UnLockedBalanceToLimitEvent _this;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.unLockedBalanceToLimitEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(event -> _this.processEvent(event));
    }

    private void processEvent(VRToken.UnLockedBalanceToLimitEventResponse event) {
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
        recordFlow(up, UnitConvertUtils.toEther(event.limitBalanceOf),UnitConvertUtils.toEther(event.lockedBalanceOf));
        tradeRecordEventListener.sendTradeRecordEvent(up);
    }

    private void recordFlow(EthTradeRecord up, BigDecimal limitBalanceOf, BigDecimal lockedBalanceOf){
        ethAssetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                limitBalanceOf,
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
