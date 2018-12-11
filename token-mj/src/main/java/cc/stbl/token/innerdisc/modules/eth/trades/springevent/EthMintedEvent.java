package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;
import org.springframework.context.ApplicationEvent;

public class EthMintedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public EthMintedEvent(Object source) {
        super(source);
    }
}
