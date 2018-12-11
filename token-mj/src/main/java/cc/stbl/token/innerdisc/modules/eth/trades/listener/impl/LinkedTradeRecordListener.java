package cc.stbl.token.innerdisc.modules.eth.trades.listener.impl;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthTradeRecordListener;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LinkedTradeRecordListener extends EthTradeRecordListener {

    @Autowired
    private TwdLinkedTradeService twdLinkedTradeService;

    @Override
    protected boolean listenTradeType(Integer bType) {
        return Objects.equals(BEnum.LINKED_BUY_TRANSFER.type, bType) || Objects.equals(BEnum.LINKED_SELL_TRANSFER.type, bType);
    }

    @Override
    public void onSuccess(EthTradeRecord tradeRecord) {
        Integer bType = tradeRecord.getTradeType();
        if(Objects.equals(BEnum.LINKED_BUY_TRANSFER.type, bType)) {
            twdLinkedTradeService.buyAssetSuccess(tradeRecord.getTradeNo());
        } else {
            twdLinkedTradeService.sellAssetSuccess(tradeRecord.getTradeNo());
        }
    }


}
