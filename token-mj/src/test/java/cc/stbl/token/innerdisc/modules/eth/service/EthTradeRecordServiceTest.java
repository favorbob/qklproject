package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.admin.trades.EthTradeRecordProto;
import com.ks.common.datastore.model.Pager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EthTradeRecordServiceTest extends AbstractTestCase {

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Test
    public void getPager() {
        EthTradeRecordProto.RequestListCondition condition = new EthTradeRecordProto.RequestListCondition();
        condition.setPageNo(1);
        condition.setPageSize(1);
        Pager<EthTradeRecordProto.ResponseGetTradeRecords> pager = tradeRecordService.getPager(condition);
        System.out.println("EthTradeRecordServiceTest.getPager.getTotalCount --->" + pager.getTotalCount());
        List<EthTradeRecordProto.ResponseGetTradeRecords> list = pager.getList();
        for (EthTradeRecordProto.ResponseGetTradeRecords records : list){
            List<EthTradeRecordProto.ResponseDetail> details = tradeRecordService.getDetail(records.getId());
            for (EthTradeRecordProto.ResponseDetail detail : details ){
                System.out.println("EthTradeRecordServiceTest.getPager.detail --->" + detail);
            }
        }
    }

    @Test
    public void getDetail() {
        List<EthTradeRecordProto.ResponseDetail> list = tradeRecordService.getDetail("7cdb6768a49e4d7aafca0b15a8a38b0f");
        for (EthTradeRecordProto.ResponseDetail info : list){
            System.out.println("EthTradeRecordServiceTest.getBuyAndSellDetail --->" + info);
        }
    }
}