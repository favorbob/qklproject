package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthReturnedIntegralListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EthIntegralRebateListener implements ApplicationListener<EthIntegralRebateEvent> {

    @Autowired(required = false)
    private List<EthReturnedIntegralListener> listeners;

    @Autowired
    private ApplicationContext applicationContext;

    public void sendTradeRecordEvent(EthReturnedIntegral returnedIntegral){
        new Thread(() -> applicationContext.publishEvent(new EthIntegralRebateEvent(returnedIntegral))).start();
    }

    @Override
    public void onApplicationEvent(EthIntegralRebateEvent event) {
        if(listeners == null) return;
        EthReturnedIntegral record = (EthReturnedIntegral) event.getSource();
        for (EthReturnedIntegralListener listener : listeners) {
            try {
                if(listener.shouldListen(record)) {
                    listener.onSuccess(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
