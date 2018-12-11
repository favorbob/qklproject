/**
* generator by mybatis plugin Fri Aug 17 17:39:58 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.dao.IntegralFlowMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.IntegralFlow;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.IntegralFlowEventListener;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class IntegralFlowService extends DataStoreServiceImpl<String, IntegralFlow, IntegralFlowMapper> {

    @Autowired
    private IntegralFlowEventListener flowEventListener;

    public void createFlow(String transactionHash, Long blockNumber, BEnum typeEnum, String tradeNo, Date success,
                           BigDecimal integralOfTo, BigDecimal tradeIntegral, String userId, Integer tradeFlowType) {
        IntegralFlow flow = new IntegralFlow();
        flow.setId(JavaUUIDGenerator.get());
        flow.setAtBlockNumber(blockNumber);
        flow.setBusinessType(typeEnum.type);
        flow.setTradeNo(tradeNo);
        flow.setCreateTime(success);
        flow.setRemainIntegral(integralOfTo);
        flow.setTradeIntegral(tradeIntegral);
        flow.setUserId(userId);
        flow.setTradeType(tradeFlowType);
        flow.setTransactionHash(transactionHash);
        this.add(flow);
        flowEventListener.sendIntegralFlowEvent(flow);
    }
}