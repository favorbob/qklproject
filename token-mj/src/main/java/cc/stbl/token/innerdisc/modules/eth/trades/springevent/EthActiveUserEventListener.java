package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthActiveUserListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class EthActiveUserEventListener implements ApplicationListener<EthActiveUserEvent> {

    @Autowired(required = false)
    private List<EthActiveUserListener> listeners;

    @Autowired
    private ApplicationContext applicationContext;


    public void sendIntegralAmplifyEvent(EthIntegralAmplify integralAmplify){
        new Thread(() -> applicationContext.publishEvent(new EthActiveUserEvent(integralAmplify))).start();
    }

    @Override
    public void onApplicationEvent(EthActiveUserEvent event) {
        if(listeners == null) return;
        EthIntegralAmplify record = (EthIntegralAmplify) event.getSource();
        for (EthActiveUserListener listener : listeners) {
            try {
                if(listener.shouldListen(record)) {
                    if(Objects.equals(record.getStatus(), TradeConsts.TRADE_STATUS_SUCCESS)) listener.onSuccess(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
