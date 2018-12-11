package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.modules.eth.entity.EthContractDeployed;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VrTokenEventManager implements InitializingBean {

    @Autowired
    private List<AbstractVrTokenEvent> eventList;

    @Autowired
    private VrTokenManager vrTokenManager;

    public void startListener(){
        EthContractDeployed deployed = vrTokenManager.getLastDeployed();
        if(deployed == null) return;
        VRToken vrToken = vrTokenManager.getLastVrToken();
        startListener(deployed,vrToken);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startListener();
    }

    public void startListener(EthContractDeployed deployed, VRToken vrToken) {
        for (AbstractVrTokenEvent vrTokenEvent : eventList) {
            vrTokenEvent.listenerVrTokenEvent(deployed,vrToken);
        }
    }
}
