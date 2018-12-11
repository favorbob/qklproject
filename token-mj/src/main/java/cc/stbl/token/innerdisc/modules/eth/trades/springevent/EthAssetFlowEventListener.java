package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthAssetFlowListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EthAssetFlowEventListener implements ApplicationListener<EthAssetFlowEvent>{

    @Autowired(required = false)
    private List<EthAssetFlowListener> flowListeners;

    @Autowired
    private ApplicationContext applicationContext;

    public void sendEthAssetFlowEvent(EthAssetFlow assetFlow){
        new Thread(() -> applicationContext.publishEvent(new EthAssetFlowEvent(assetFlow))).start();
    }

    @Override
    public void onApplicationEvent(EthAssetFlowEvent event) {
        if(flowListeners == null) return;
        EthAssetFlow ethAssetFlow = (EthAssetFlow) event.getSource();
        for (EthAssetFlowListener flowListener : flowListeners) {
            try {
                if(flowListener.shouldListen(ethAssetFlow)){
                    flowListener.onFlowWrited(ethAssetFlow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
