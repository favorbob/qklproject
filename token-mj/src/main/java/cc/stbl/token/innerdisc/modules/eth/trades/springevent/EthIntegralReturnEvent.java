package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import org.springframework.context.ApplicationEvent;

public class EthIntegralReturnEvent extends ApplicationEvent{
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public EthIntegralReturnEvent(Object source) {
        super(source);
    }
}
