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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class TransferExtEvent extends AbstractVrTokenEvent {

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private TransferExtEvent transferExtEvent;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.transferExtEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST).subscribe(event ->{
            transferExtEvent.processEvent(event);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void processEvent(VRToken.TransferExtEventResponse event){
        String tradeId = event.tradeNo;
        logger.info("TransferExt_trade_record_id={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
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
        setStartValue(up.getAtBlockNumber() - 1);
        BigDecimal fromTotalBalance= UnitConvertUtils.toEther(event.balanceOfFrom);
        BigDecimal toTotalBalance= UnitConvertUtils.toEther(event.balanceOfTo);
        recordFlow(up,fromTotalBalance,toTotalBalance);
        tradeRecordEventListener.sendTradeRecordEvent(up);
    }

    private void recordFlow(EthTradeRecord up, BigDecimal fromTotalBalance, BigDecimal toTotalBalance) {
        if (StringUtils.isNotEmpty(up.getFromUserId())) {
            ethAssetFlowService.createFlow(up.getTradeNo(),
                    up.getFromUserId(),
                    up.getTradeAmount(),
                    fromTotalBalance,
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
                    toTotalBalance,
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
