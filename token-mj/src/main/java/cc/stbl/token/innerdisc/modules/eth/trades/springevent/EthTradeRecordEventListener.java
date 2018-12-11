package cc.stbl.token.innerdisc.modules.eth.trades.springevent;

import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthTradeRecordListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class EthTradeRecordEventListener implements ApplicationListener<EthTradeRecordEvent> {

    @Autowired(required = false)
    private List<EthTradeRecordListener> listeners;

    @Autowired
    private ApplicationContext applicationContext;


    public void sendTradeRecordEvent(EthTradeRecord tradeRecord){
        new Thread(() -> applicationContext.publishEvent(new EthTradeRecordEvent(tradeRecord))).start();
    }

    @Override
    public void onApplicationEvent(EthTradeRecordEvent event) {
        if(listeners == null) return;
        EthTradeRecord record = (EthTradeRecord) event.getSource();
        for (EthTradeRecordListener listener : listeners) {
            try {
                if(listener.shouldListen(record)) {
                    if(Objects.equals(record.getTradeStatus(), TradeConsts.TRADE_STATUS_SUCCESS)) listener.onSuccess(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
