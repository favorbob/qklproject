package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;

public abstract class EthMintRecordListener {

    public boolean shouldListen(EthMintRecord mintRecord){
        return true;
    }

    public abstract void onSuccess(EthMintRecord mintRecord);
}
