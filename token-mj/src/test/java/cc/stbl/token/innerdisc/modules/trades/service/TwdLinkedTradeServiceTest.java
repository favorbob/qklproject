package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.payment.service.MyAssetsService;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTrade;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeProto;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeRecordProto;
import cc.stbl.token.innerdisc.restapi.app.trades.LinkedTradeRecordRestController;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class TwdLinkedTradeServiceTest extends AbstractTestCase{

    @Autowired
    private TwdLinkedTradeService linkedTradeService;

    @Autowired
    private TwdLinkedTradeRecordService twdLinkedTradeRecordService;


    @Autowired
    private LinkedTradeRecordRestController restController;

    @Autowired
    private MyAssetsService myAssetsService;

    @Test
    public void testApplySell() throws Exception {
        mockAppLogin("leon");
        linkedTradeService.applyLinkedSell(500,8d,2);
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void testBuyAsset() throws Exception {
        mockAppLogin("1310000001");
        linkedTradeService.buyAsset("212994248474234880",20,2);
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void name() throws Exception {
        mockAppLogin("18934790727");
        dump();
    }

    @Test
    public void buySysAssetTest() throws Exception {
        mockAppLogin("1310000001");
        linkedTradeService.buySysAsset(60);
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void sendTipMessage() throws Exception{
        mockAppLogin("1310000001");
        twdLinkedTradeRecordService.sendTipMessage("212898926506938368");
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void confirmTradeTest() throws Exception{
        mockAppLogin("leon");
        linkedTradeService.confirmTrade("212898926506938368");
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void applyLinkedBuyTest() throws InterruptedException {
        mockAppLogin("1310000001");
        linkedTradeService.applyLinkedBuy(28,5d,2);
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void requestToUserRecord() throws Exception {
        mockAppLogin("leon");
        LinkedTradeRecordProto.RequestToUserRecord request = new LinkedTradeRecordProto.RequestToUserRecord();
        request.setType(1);
        request.setPageNo(1);
        request.setPageSize(100);
        System.out.println(JSON.toJSONString(restController.toUserRecord(request)));
    }

    private void dump() {
        System.out.println(JSON.toJSONString(myAssetsService.myAssetsHome()));
    }

    @Test
    public void sellAssetPageData(){
        String userId = "083b73708c1a409a81a628d6b2e68169";
        String likedId = "210330347957784576";
        LinkedTradeProto.ResponseSellAssetPageData data = twdLinkedTradeRecordService.sellAssetPageData(likedId, userId);
        System.out.println("TwdLinkedTradeServiceTest.sellAssetPageData ---> " + data);
    }

    @Test
    public void userTradeList() throws Exception {
        mockAppLogin("18934790727");
        TwdLinkedTrade linkedTrade = new TwdLinkedTrade();
        linkedTrade.setCreateDate(new Date());
        linkedTrade.setUserId(ShiroUtils.getLoginUserId());
//        linkedTrade.setLinkedStatus(TradeConsts.TRADE_STATUS_SUCCESS);
        linkedTrade.setLinkedType(TradeConsts.LINKED_TYPE_SELL);
        linkedTrade.desc("l.createDate");
        List<TwdLinkedTrade> list = linkedTradeService.findUserLinkedList(linkedTrade,1,100);
        System.out.println(list);
    }

    @Test
    public void buyAssetPageData(){
        String userId = "083b73708c1a409a81a628d6b2e68169";
        String likedId = "210330347957784576";
        LinkedTradeProto.ResponseBuyAssetPageData data = twdLinkedTradeRecordService.buyAssetPageData(likedId, userId);
        System.out.println("TwdLinkedTradeServiceTest.buyAssetPageData ---> " + data);
    }


}
