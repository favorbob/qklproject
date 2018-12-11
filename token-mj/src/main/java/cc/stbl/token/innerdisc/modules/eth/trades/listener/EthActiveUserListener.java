package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;

public abstract class EthActiveUserListener {

    public boolean shouldListen(EthIntegralAmplify integralAmplify){
        return true;
    }

    public abstract void onSuccess(EthIntegralAmplify integralAmplify);
}
