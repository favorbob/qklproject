/**
* generator by mybatis plugin Wed Aug 29 14:59:43 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.eth.dao.EthSysAssetFlowMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthSysAssetFlow;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class EthSysAssetFlowService extends DataStoreServiceImpl<String, EthSysAssetFlow, EthSysAssetFlowMapper> {

    public EthSysAssetFlow createFlow(String tradeNo, String userId, BigDecimal tradeAmount,
                                   BigDecimal remainAmount, Integer flowType, Integer businessType, Integer isPlus,
                                   String transactionHash, Long atBlockNumber){
        EthSysAssetFlow ethAssetFlow= new EthSysAssetFlow();
        ethAssetFlow.setTradeNo(tradeNo);
        ethAssetFlow.setUserId(userId);
        ethAssetFlow.setTradeAmount(tradeAmount);
        ethAssetFlow.setRemainAmount(remainAmount);
        ethAssetFlow.setTradeType(flowType);
        ethAssetFlow.setBusinessType(businessType);
        ethAssetFlow.setIsPlus(isPlus);
        ethAssetFlow.setTransactionHash(transactionHash);
        ethAssetFlow.setAtBlockNumber(atBlockNumber);
        ethAssetFlow.setId(JavaUUIDGenerator.get());
        ethAssetFlow.setCreateTime(new Date());
        add(ethAssetFlow);
        return ethAssetFlow;
    }
}