package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserDailyIncomeStatistics;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RebateUserDailyIncomeStatisticsServiceTest extends AbstractTestCase {

    @Autowired
    private RebateUserDailyIncomeStatisticsService rebateUserDailyIncomeStatisticsService;

    @Test
    public void countUserDailyIncome() {
        String stringDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        rebateUserDailyIncomeStatisticsService.countUserDailyIncome(stringDate);
        List<RebateUserDailyIncomeStatistics> all = rebateUserDailyIncomeStatisticsService.findAll();
        for (RebateUserDailyIncomeStatistics vo : all){
            System.out.println("RebateUserDailyIncomeStatisticsServiceTest.countUserDailyIncome----->" + vo);
        }
    }
}