package cc.stbl.token.innerdisc.modules.eth.trades.listener;

import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;

public abstract class EthAssetFlowListener {

    public boolean shouldListen(EthAssetFlow ethAssetFlow){
        return true;
    }

    public abstract void onFlowWrited(EthAssetFlow ethAssetFlow);
}
