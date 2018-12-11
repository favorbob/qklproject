package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthMintRecordService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthMintedEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.Date;

@Component
public class MintedEvent extends AbstractVrTokenEvent {

    @Autowired
    private MintedEvent _this;

    @Autowired
    private EthMintRecordService mintRecordService;

    @Autowired
    private EthAssetFlowService assetFlowService;

    @Autowired
    private EthMintedEventListener mintedEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.mintEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST).subscribe(event ->{
            _this.processEvent(event);
        });
    }

    @Transactional
    protected void processEvent(VRToken.MintEventResponse event) {
        String tradeId = event.tradeNo;
        logger.info("TransferExt_trade_record_id={}`transactionHash={}",event.tradeNo,event.log.getTransactionHash());
        EthMintRecord up = mintRecordService.get(tradeId);
        if(up == null) return;;
        if(TradeConsts.TRADE_STATUS_SUCCESS.equals(up.getMintStatus()) ) return;
        up.setMintStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        EthBlock.Block block = getBlock(event.log.getBlockNumber());
        up.setMintSuccessDate(block == null ? new Date() : new Date(block.getTimestamp().longValue() * 1000));
        up.setAtBlockNumber(event.log.getBlockNumber().longValue());
        up.setTransactionHash(event.log.getTransactionHash());
        mintRecordService.update(up);
        assetFlowService.createFlow(
                tradeId,up.getUserId(),up.getMintAmount(),
                UnitConvertUtils.toEther(vrTokenManager.balanceOf(up.getAddress())),
                    TradeConsts.FLOW_TYPE_BALANCE, BEnum.BALANCE_MINT.type,TradeConsts.TRADE_FLOW_BTYPE_INCOME,up.getTransactionHash(),up.getAtBlockNumber(),up.getRemark()
                );
        setStartValue(up.getAtBlockNumber() - 1);
        mintedEventListener.sendMintRecordEvent(up);
    }
}
