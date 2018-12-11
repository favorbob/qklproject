package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthIntegralFlowListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegralFlowEventListener implements ApplicationListener<IntegralFlowEvent> {

    @Autowired(required = false)
    private List<EthIntegralFlowListener> flowListeners;

    @Autowired
    private ApplicationContext applicationContext;


    public void sendIntegralFlowEvent(IntegralFlow integralFlow){
        new Thread(() -> applicationContext.publishEvent(new IntegralFlowEvent(integralFlow))).start();
    }

    @Override
    public void onApplicationEvent(IntegralFlowEvent event) {
        if(flowListeners == null) return;
        IntegralFlow flow = (IntegralFlow) event.getSource();
        for (EthIntegralFlowListener flowListener : flowListeners) {
            try {
                if(flowListener.shouldListen(flow)){
                    flowListener.onFlowWrited(flow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
