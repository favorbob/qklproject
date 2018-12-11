package cc.stbl.token.innerdisc.modules.eth.trades.listener.impl;

import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.listener.EthTradeRecordListener;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LinkedTradeListener extends EthTradeRecordListener {

    @Autowired
    private TwdLinkedTradeService linkedTradeService;

    @Override
    protected boolean listenTradeType(Integer bType) {
        return BEnum.LINKED_LOCK.type == bType || BEnum.LINKED_UN_LOCK.type == bType;
    }

    @Override
    public void onSuccess(EthTradeRecord tradeRecord) {
        String linkid = tradeRecord.getTradeNo();
        TwdLinkedTrade update = new TwdLinkedTrade();
        update.setId(linkid);
        update.setUpdateDate(new Date());
        update.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        linkedTradeService.update(update);
    }


}
