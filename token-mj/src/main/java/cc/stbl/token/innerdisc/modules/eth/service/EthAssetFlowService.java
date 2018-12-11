/**
* generator by mybatis plugin Fri Jul 13 17:29:06 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.modules.eth.dao.EthAssetFlowMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.trades.springevent.EthAssetFlowEventListener;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EthAssetFlowService extends DataStoreServiceImpl<String, EthAssetFlow, EthAssetFlowMapper> {

    @Autowired
    private EthAssetFlowMapper ethAssetFlowMapper;

    @Autowired
    private EthAssetFlowEventListener flowEventListener;

    public Pager<EthAssetFlow> findPager(EthAssetFlow query, Integer pageNo, Integer pageSize) {
        Pager<EthAssetFlow> pager = super.findPage(query,pageNo,pageSize);
        pager.setList(pager.getList().stream().flatMap(flow -> {
            flow.setTradeTypeName(BEnum.getNameByCode(flow.getBusinessType()));
            return Stream.of(flow);
        }).collect(Collectors.toList()));
        return pager;
    }

    private EthAssetFlow createFlow(EthAssetFlow ethAssetFlow){
        ethAssetFlow.setId(JavaUUIDGenerator.get());
        ethAssetFlow.setCreateTime(new Date());
        add(ethAssetFlow);
        flowEventListener.sendEthAssetFlowEvent(ethAssetFlow);
        return ethAssetFlow;
    }

    public EthAssetFlow createFlow(String tradeNo,String userId,BigDecimal tradeAmount,
            BigDecimal remainAmount,Integer flowType,Integer businessType,boolean isPlus,
            String transactionHash,Long atBlockNumber,String remark){
        EthAssetFlow ethAssetFlow=new EthAssetFlow();
        ethAssetFlow.setTradeNo(tradeNo);
        ethAssetFlow.setUserId(userId);
        ethAssetFlow.setTradeAmount(tradeAmount);
        ethAssetFlow.setRemainAmount(remainAmount);
        ethAssetFlow.setTradeType(flowType);
        ethAssetFlow.setBusinessType(businessType);
        ethAssetFlow.setIsPlus(isPlus);
        ethAssetFlow.setTransactionHash(transactionHash);
        ethAssetFlow.setAtBlockNumber(atBlockNumber);
        ethAssetFlow.setRemark(remark);
        return createFlow(ethAssetFlow);
    }

    public List<EthAssetFlow> getUserDailyIncomeList(String stringDate){
        return ethAssetFlowMapper.getUserDailyIncomeList(stringDate);
    }

}