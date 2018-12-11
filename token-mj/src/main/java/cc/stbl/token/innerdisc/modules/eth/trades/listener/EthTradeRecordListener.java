package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;

public abstract class EthTradeRecordListener {
    public boolean shouldListen(EthTradeRecord tradeRecord){
        return listenTradeType(tradeRecord.getTradeType());
    }
    protected abstract boolean listenTradeType(Integer bType);
    public abstract void onSuccess(EthTradeRecord tradeRecord);
}
