/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserDailyIncomeStatistics;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RebateUserDailyIncomeStatisticsMapper extends IDataStoreMapper<String, RebateUserDailyIncomeStatistics> {

    RebateUserDailyIncomeStatistics getUserIncomeByUserId(@Param("userId")String userId);

    List<RebateUserDailyIncomeStatistics> getParentUserIncome();

    @MapKey("userId")
    Map<String, RebateUserDailyIncomeStatistics> getChildUserIncome();
}