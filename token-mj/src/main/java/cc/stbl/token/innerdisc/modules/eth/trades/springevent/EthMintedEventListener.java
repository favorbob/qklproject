package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthMintRecordListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class EthMintedEventListener implements ApplicationListener<EthMintedEvent> {

    @Autowired(required = false)
    private List<EthMintRecordListener> listeners;

    @Autowired
    private ApplicationContext applicationContext;


    public void sendMintRecordEvent(EthMintRecord tradeRecord){
        new Thread(() -> applicationContext.publishEvent(new EthMintedEvent(tradeRecord))).start();
    }

    @Override
    public void onApplicationEvent(EthMintedEvent event) {
        if(listeners == null) return;
        EthMintRecord record = (EthMintRecord) event.getSource();
        for (EthMintRecordListener listener : listeners) {
            try {
                if(listener.shouldListen(record)) {
                    if(Objects.equals(record.getMintStatus(), TradeConsts.TRADE_STATUS_SUCCESS)) listener.onSuccess(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
