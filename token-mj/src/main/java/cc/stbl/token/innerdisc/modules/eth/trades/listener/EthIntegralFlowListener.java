package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;

public abstract class EthIntegralFlowListener {

    public boolean shouldListen(IntegralFlow integralFlow){
        return true;
    }

    public abstract void onFlowWrited(IntegralFlow integralFlow);
}
