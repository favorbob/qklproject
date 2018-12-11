package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;

public abstract class EthIntegralReturnedListener {

    public abstract void onSuccess(EthReturnedIntegral returnedIntegral);

    public boolean shouldListen(EthReturnedIntegral record) {
        return true;
    }
}
