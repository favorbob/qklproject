package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthReturnedIntegralService;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthIntegralRebateListener;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthIntegralReturnListener;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.Date;


@Component
public class IntegralReturnEvent extends AbstractVrTokenEvent {


    @Autowired
    private EthReturnedIntegralService returnedIntegralService;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private EthAssetFlowService assetFlowService;

    @Autowired
    private EthIntegralReturnListener rebateListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.integralReturnEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
             String lockId = "distributedLock:event:IntegralReturnEvent:" + event.tradeNo;
             String requestId = JavaUUIDGenerator.get();
             boolean lock =  distributedLock.tryLock(lockId, requestId,300) ;
             try {
                 if(lock) processEvent(event);
             } finally {
                 if(lock) distributedLock.unLock(lockId,requestId);
             }
        });
    }

    protected void processEvent(VRToken.IntegralReturnEventResponse event){
        logger.info("listener_IntegralReturnEventResponse_{}", JSON.toJSONString(event));
        EthReturnedIntegral ri = returnedIntegralService.get(event.tradeNo);
        if(ri.getStatus() == TradeConsts.TRADE_STATUS_SUCCESS) return;
        ri.setAtBlockNumber(event.log.getBlockNumber().longValue());
        ri.setTransactionHash(event.log.getTransactionHash());
        ri.setStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        EthBlock.Block block = getBlock(event.log.getBlockNumber());
        ri.setSuccessDate(block == null ? new Date() : new Date(block.getTimestamp().longValue() * 1000));
        returnedIntegralService.update(ri);
               /* EthIntegralAmplify amplify = amplifyService.get(ri.getAmplifyId());
                amplify.setReturnedIntegral(amplify.getReturnedIntegral().saveOrUpdate(ri.getIntegral()));
                if(amplify.getReturnedIntegral().doubleValue() >= amplify.getTotalIntegral().doubleValue()) amplify.setStatus(2);
                amplifyService.update(amplify);*/
        setStartValue(ri.getAtBlockNumber() - 1);

        //积分流水
        integralFlowService.createFlow(ri.getTransactionHash(),ri.getAtBlockNumber(), BEnum.INTEGRAL_REBATE,
                ri.getId(),ri.getSuccessDate(),
                UnitConvertUtils.toEther(event.integralOf)
                ,ri.getIntegral(),ri.getUserId(),TradeConsts.TRADE_FLOW_TYPE_EXPEND
        );
        //资产流水
        assetFlowService.createFlow(
                ri.getId(),ri.getUserId(),ri.getBalance(),
                UnitConvertUtils.toEther(event.aiicOf),
                TradeConsts.FLOW_TYPE_BALANCE,
                BEnum.INTEGRAL_REBATE.type,
                TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                ri.getTransactionHash(),ri.getAtBlockNumber(),
                BEnum.INTEGRAL_REBATE.remark
        );
        //受限资产流水
        assetFlowService.createFlow(
                ri.getId(),ri.getUserId(),ri.getLimitedBalance(),
                UnitConvertUtils.toEther(event.mjOf),
                TradeConsts.FLOW_TYPE_LIMITED_BALANCE,
                BEnum.INTEGRAL_REBATE.type,
                TradeConsts.TRADE_FLOW_BTYPE_INCOME,
                ri.getTransactionHash(),ri.getAtBlockNumber(),
                BEnum.INTEGRAL_REBATE.remark
        );
        rebateListener.sendTradeRecordEvent(ri);
    }

}
