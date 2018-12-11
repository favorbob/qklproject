/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.RebateUserRebateConfigMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserDailyIncomeStatistics;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserRebateConfig;
import cc.stbl.token.innerdisc.modules.eth.trades.accelerate.VrTokenIntegralAdapter;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RebateUserRebateConfigService extends DataStoreServiceImpl<String, RebateUserRebateConfig, RebateUserRebateConfigMapper> {

    @Autowired
    private RebateUserDailyIncomeStatisticsService rebateUserDailyIncomeStatisticsService;
    @Autowired
    private RebateConfigService rebateConfigService;

    @Autowired
    private SiteSettingService siteSettingService;

    public void calculateUserRebateConfig(){
        // 查询所有比例配置
        List<RebateConfig> rebateConfigList = rebateConfigService.findAll();
        TreeMap<Float, Float> treeMap = new TreeMap<>();
        for (RebateConfig rebateConfig : rebateConfigList){
            treeMap.put(rebateConfig.getRatio(), rebateConfig.getRebateRatio());
        }
        // 查询上线转入的资产总和
        List<RebateUserDailyIncomeStatistics> parentIncomeList = rebateUserDailyIncomeStatisticsService.getParentUserIncome();
        // 查询下线转入的资产总和
        Map<String, RebateUserDailyIncomeStatistics> childUserIncomeMap = rebateUserDailyIncomeStatisticsService.getChildUserIncome();
        // 下线/上线 获得上下线资产比例
        List<RebateUserRebateConfig> userRebateConfigs = new ArrayList<>();
        for (RebateUserDailyIncomeStatistics vo : parentIncomeList){
            Float ratio;
            String id = vo.getUserId();
            RebateUserDailyIncomeStatistics incomeStatistics = childUserIncomeMap.get(id);
            if (incomeStatistics != null){
                // 上线资产
                BigDecimal parentIncome = vo.getIncome();
                // 下线资产
                BigDecimal childIncome = incomeStatistics.getIncome();
                // 资产比例
                BigDecimal divide = childIncome.divide(parentIncome);
                // 返利比例
                Float key = treeMap.floorKey(Float.valueOf(divide.toPlainString()));
                ratio = treeMap.get(key);
                if(ratio == null) ratio = siteSettingService.getIntegralRebateRatio().floatValue();
                RebateUserRebateConfig config = new RebateUserRebateConfig();
                config.setCurrRebateRatio(ratio);
                config.setUserId(vo.getUserId());
                userRebateConfigs.add(config);
            }
        }
        this.batchAddOrUpdate(userRebateConfigs);
    }

    public void batchAddOrUpdate(List<RebateUserRebateConfig> userRebateConfigs) {
        if(userRebateConfigs.size() == 0) return;
        List<List<RebateUserRebateConfig>> listList = super.splitList(userRebateConfigs, 1000);
        for (List<RebateUserRebateConfig> currentBatch : listList) {
            this.mapper.batchAddOrUpdate(currentBatch);
        }
    }


}