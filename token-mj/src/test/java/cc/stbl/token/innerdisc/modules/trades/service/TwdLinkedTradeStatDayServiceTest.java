package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TwdLinkedTradeStatDayServiceTest extends AbstractTestCase {

    @Autowired
    private TwdLinkedTradeStatDayService twdLinkedTradeStatDayService;

    @Test
    public void addTwdLinkedTradeStatDay() {
        twdLinkedTradeStatDayService.addTwdLinkedTradeStatDay();
    }

    @Test
    public void getStatisticalTradeAsset() {
        TwdLinkedTradeStatDay statisticalTradeAsset = twdLinkedTradeStatDayService.getStatisticalTradeAsset();
        System.out.println("TwdLinkedTradeStatDayServiceTest.getStatisticalTradeAsset --->" + statisticalTradeAsset);
    }
}