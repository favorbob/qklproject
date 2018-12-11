/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.RebateUserDailyIncomeStatisticsMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserDailyIncomeStatistics;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RebateUserDailyIncomeStatisticsService extends DataStoreServiceImpl<String, RebateUserDailyIncomeStatistics, RebateUserDailyIncomeStatisticsMapper> {

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    public void countUserDailyIncome(String strinDate){
        List<EthAssetFlow> list = ethAssetFlowService.getUserDailyIncomeList(strinDate);
        if (list != null && list.size() > 0){
            List<RebateUserDailyIncomeStatistics> incomeList = new ArrayList<>();
            for (EthAssetFlow ethAssetFlow : list){
                RebateUserDailyIncomeStatistics incomeStatistics = new RebateUserDailyIncomeStatistics();
                incomeStatistics.setId(UUID.randomUUID().toString());
                incomeStatistics.setStatisticsDate(ethAssetFlow.getCreateTime());
                incomeStatistics.setUserId(ethAssetFlow.getUserId());
                incomeStatistics.setIncome(ethAssetFlow.getTradeAmount());
                incomeList.add(incomeStatistics);
            }
            mapper.batchInsertSelective(incomeList);
        }
    }

    public RebateUserDailyIncomeStatistics getUserIncome(String userId){
        return mapper.getUserIncomeByUserId(userId);
    }

    public List<RebateUserDailyIncomeStatistics> getParentUserIncome(){
        return mapper.getParentUserIncome();
    }

    public Map<String, RebateUserDailyIncomeStatistics> getChildUserIncome(){
        return mapper.getChildUserIncome();
    }
}