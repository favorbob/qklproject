package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthIntegralAmplifyService;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthIntegralAmplifyEventListener;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.Date;

@Component
public class IntegralLimitedAmplifyEvent extends AbstractVrTokenEvent{


    @Autowired
    private EthIntegralAmplifyService amplifyService;

    @Autowired
    private IntegralLimitedAmplifyEvent amplifyEvent;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private EthAssetFlowService assetFlowService;

    @Autowired
    private EthIntegralAmplifyEventListener amplifyEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.integralLimitedAmplifyEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(ev -> {
                    amplifyEvent.processEvent(ev,vrToken);
                });
    }

    @Transactional
    public void processEvent(VRToken.IntegralLimitedAmplifyEventResponse ev,VRToken vrToken) {
        logger.info("integral_limit_Amplify_event_{}", JSON.toJSONString(ev));
        EthIntegralAmplify amplify = amplifyService.get(ev.tradeNo);
        if(amplify.getStatus() != TradeConsts.TRADE_STATUS_PROCESSING) return;
        amplify.setId(ev.tradeNo);
        amplify.setTotalToken(UnitConvertUtils.toEther(ev.amount));
        amplify.setTotalIntegral(UnitConvertUtils.toEther(ev.integral));
        amplify.setAtBlockNumber(ev.log.getBlockNumber().longValue());
        amplify.setTransactionHash(ev.log.getTransactionHash());
        amplify.setStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        amplifyService.update(amplify);
        setStartValue(ev.log.getBlockNumber().longValue() - 1);

        EthBlock.Block block = getBlock(ev.log.getBlockNumber());
        Date successTime = block == null ? new Date() : new Date(block.getTimestamp().longValue() * 1000);
        integralFlowService.createFlow(
                ev.log.getTransactionHash(),
                ev.log.getBlockNumber().longValue(),
                BEnum.AMPLIFY,
                amplify.getId(),successTime,UnitConvertUtils.toEther(vrTokenManager.integralOf(ev.targetAddress)),
                UnitConvertUtils.toEther(ev.integral),amplify.getUserId(),TradeConsts.TRADE_FLOW_TYPE_INCOME
        );
        assetFlowService.createFlow(amplify.getId(),amplify.getUserId(),UnitConvertUtils.toEther(ev.amount),UnitConvertUtils.toEther(vrTokenManager.limitBalanceOf(ev.targetAddress,vrToken))
                ,amplify.getTokenType(),BEnum.AMPLIFY.type,!TradeConsts.TRADE_FLOW_BTYPE_INCOME,ev.log.getTransactionHash(),
                ev.log.getBlockNumber().longValue(),"积分兑换"
        );
        amplifyEventListener.sendIntegralAmplifyEvent(amplify);
    }
}
