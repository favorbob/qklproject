package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
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
public class TransferFromAmplifyEvent extends AbstractVrTokenEvent {

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private TransferFromAmplifyEvent _this;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private EthTradeRecordEventListener tradeRecordEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.transferFromAmplifyEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(event-> _this.processEvent(event));
    }

    @Transactional
    public void processEvent(VRToken.TransferFromAmplifyEventResponse event){
        String tradeId = event.tradeNo;
        logger.info("TransferFromAmplifyEvent_trade_record_id={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
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
        printTradeWater(up, UnitConvertUtils.toEther(vrTokenManager.balanceOf(up.getFromAddress())),
                UnitConvertUtils.toEther(event.integral),
                UnitConvertUtils.toEther(vrTokenManager.integralOf(up.getToAddress())));
        setStartValue(up.getAtBlockNumber() - 1);
        tradeRecordEventListener.sendTradeRecordEvent(up);
    }


    /**
     * TODO 记录转账用户的 资产流水
     * TODO 记录收账用户的 积分流水
     * @param record 交易记录
     * @param balanceOfFrom 发起用户的资产总数
     * @param integralOfTo 接收用户的积分总数
     */
    private void printTradeWater(EthTradeRecord record, BigDecimal balanceOfFrom,BigDecimal tradeIntegral ,BigDecimal integralOfTo) {
        ethAssetFlowService.createFlow(
                record.getTradeNo(),
                record.getFromUserId(),
                record.getTradeAmount(),
                balanceOfFrom,
                record.getFromFlowType(),
                record.getTradeType(),
                !TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                record.getTransactionHash(),
                record.getAtBlockNumber(),
                record.getFromRemark()
        );

        integralFlowService.createFlow(
                record.getTransactionHash(),
                record.getAtBlockNumber(),
                BEnum.TRANSFER_AMPLIFY,
                record.getTradeNo(),
                record.getSuccessDate(),
                integralOfTo,
                tradeIntegral,record.getToUserId(),
                TradeConsts.TRADE_FLOW_TYPE_INCOME
        );
    }
}
