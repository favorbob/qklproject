package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthIntegralAmplifyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class EthIntegralAmplifyEventListener implements ApplicationListener<EthIntegralAmplifyEvent> {

    @Autowired(required = false)
    private List<EthIntegralAmplifyListener> listeners;

    @Autowired
    private ApplicationContext applicationContext;


    public void sendIntegralAmplifyEvent(EthIntegralAmplify integralAmplify){
        new Thread(() -> applicationContext.publishEvent(new EthIntegralAmplifyEvent(integralAmplify))).start();
    }

    @Override
    public void onApplicationEvent(EthIntegralAmplifyEvent event) {
        if(listeners == null) return;
        EthIntegralAmplify record = (EthIntegralAmplify) event.getSource();
        for (EthIntegralAmplifyListener listener : listeners) {
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
