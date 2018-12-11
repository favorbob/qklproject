package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import org.springframework.context.ApplicationEvent;

public class EthTradeRecordEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public EthTradeRecordEvent(Object source) {
        super(source);
    }
}
