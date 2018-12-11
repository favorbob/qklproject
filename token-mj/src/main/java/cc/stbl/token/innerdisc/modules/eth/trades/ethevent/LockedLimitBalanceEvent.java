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
public class LockedLimitBalanceEvent extends AbstractVrTokenEvent {
    @Autowired
    private LockedLimitBalanceEvent _this;

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;


    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.lockedLimitBalanceEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(lockedTransferEventResponse -> _this.processEvent(lockedTransferEventResponse));
    }

    @Transactional
    public void processEvent(VRToken.LockedLimitBalanceEventResponse event) {
        String tradeId = event.tradeNo;
        logger.info("LockedLimitBalanceEventResponse={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
        EthTradeRecord up = tradeRecordService.get(tradeId);
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

    private void createFlow(VRToken.LockedLimitBalanceEventResponse event, EthTradeRecord up) {
        BigDecimal fromlimitBalance= UnitConvertUtils.toEther(event.balanceOf);
        BigDecimal toTotalBalance= UnitConvertUtils.toEther(event.limitBalanceOf);

        ethAssetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                fromlimitBalance,
                TradeConsts.FLOW_TYPE_LIMITED_BALANCE,
                up.getTradeType(),
                !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getFromRemark()
        );

        ethAssetFlowService.createFlow(up.getTradeNo(),
                up.getFromUserId(),
                up.getTradeAmount(),
                toTotalBalance,
                TradeConsts.FLOW_TYPE_LOCKED_BALANCE,
                up.getTradeType(),
                TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                up.getTransactionHash(),
                up.getAtBlockNumber(),
                up.getToRemark()
        );
    }
}
