package cc.stbl.token.innerdisc.modules.scheduler;

import cc.stbl.token.innerdisc.modules.basic.service.RebateUserDailyIncomeStatisticsService;
import cc.stbl.token.innerdisc.modules.basic.service.RebateUserRebateConfigService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

//@Component
public class RebateScheduler {

    private static Logger logger = LoggerFactory.getLogger(RebateScheduler.class);

    @Autowired
    private RebateUserDailyIncomeStatisticsService rebateUserDailyIncomeStatisticsService;
    @Autowired
    private RebateUserRebateConfigService rebateUserRebateConfigService;

	@Scheduled(cron = "0 30 0 * * ?") // 每天一次
	// @Scheduled(cron = "0 0/30 * * * ?") //半个时一次
    public void userRebateConfigTask(){
        logger.info("用户加速比例每日统计 start");
        this.userDailyIncomeStatistic();
        rebateUserRebateConfigService.calculateUserRebateConfig();
        logger.info("用户加速比例每日统计 end");
    }

    private void userDailyIncomeStatistic(){
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1);    //得到前一天
        String stringDate = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        rebateUserDailyIncomeStatisticsService.countUserDailyIncome(stringDate);
    }
}
