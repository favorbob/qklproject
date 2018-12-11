package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.service.EthIntegralAmplifyService;
import cc.stbl.token.innerdisc.modules.eth.service.IntegralFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthActiveUserEventListener;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.Date;

@Component
public class ActiveUserEvent extends AbstractVrTokenEvent {

    @Autowired
    private EthIntegralAmplifyService amplifyService;

    @Autowired
    private ActiveUserEvent _this;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private IntegralFlowService integralFlowService;

    @Autowired
    private EthActiveUserEventListener ethActiveUserEventListener;

    @Override
    protected void listenerEventStartBy(Long start, VRToken vrToken) {
        vrToken.activeUserEventObservable(new DefaultBlockParameterNumber(start), DefaultBlockParameterName.LATEST)
                .subscribe(ev -> {
                    _this.processEvent(ev,vrToken);
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processEvent(VRToken.ActiveUserEventResponse ev,VRToken vrToken){
        logger.info("ActiveUserEvent_{}", JSON.toJSONString(ev));
        EthIntegralAmplify amplify = amplifyService.get(ev.tradeNo);
        if(amplify == null) return;
        if(amplify.getStatus() != TradeConsts.TRADE_STATUS_PROCESSING) return;
        amplify.setId(ev.tradeNo);
        amplify.setTotalToken(UnitConvertUtils.toEther(ev.activeAmount));
        amplify.setTotalIntegral(UnitConvertUtils.toEther(ev.integralOf));
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
                BEnum.ACTIVE_USER,
                amplify.getId(),successTime,UnitConvertUtils.toEther(vrTokenManager.integralOf(ev.targetAddress)),
                UnitConvertUtils.toEther(ev.activeAmount),amplify.getUserId(),TradeConsts.TRADE_FLOW_TYPE_INCOME
        );
        ethActiveUserEventListener.sendIntegralAmplifyEvent(amplify);
    }
}
