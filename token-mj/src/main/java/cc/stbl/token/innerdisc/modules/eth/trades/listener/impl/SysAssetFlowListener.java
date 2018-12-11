package cc.stbl.token.innerdisc.modules.eth.trades.listener.impl;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.service.EthSysAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthTradeRecordListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

@Component
public class SysAssetFlowListener extends EthTradeRecordListener {

    @Autowired
    private EthSysAssetFlowService sysAssetFlowService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private Credentials sysCredentials;

    @Override
    protected boolean listenTradeType(Integer bType) {
        return BEnum.SYS_CHARGE.type == bType || BEnum.LINKED_SYS_CHARGE.type == bType;
    }

    @Override
    public void onSuccess(EthTradeRecord tradeRecord) {
        sysAssetFlowService.createFlow(tradeRecord.getTradeNo(),
                tradeRecord.getToUserId(),
                tradeRecord.getTradeAmount(),
                UnitConvertUtils.toEther(vrTokenManager.balanceOf(sysCredentials.getAddress())),
                tradeRecord.getFromFlowType(),
                tradeRecord.getTradeType(),
                TradeConsts.TRADE_FLOW_TYPE_EXPEND,
                tradeRecord.getTransactionHash(),
                tradeRecord.getAtBlockNumber()
        );
    }
}
