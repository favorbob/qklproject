package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import org.springframework.context.ApplicationEvent;

public class EthAssetFlowEvent extends ApplicationEvent{

    public EthAssetFlowEvent(Object source) {
        super(source);
    }
}
