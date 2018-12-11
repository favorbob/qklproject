package cc.stbl.token.innerdisc.modules.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import cc.stbl.token.innerdisc.modules.trades.service.TwdLinkedTradeStatDayService;

public class TwdLinkedTradeScheduler implements SimpleJob {

    private static Logger logger = LoggerFactory.getLogger(TwdLinkedTradeScheduler.class);

    @Autowired
    private TwdLinkedTradeStatDayService twdLinkedTradeStatDayService;

    public void statisticDailyTradeAsset(){
        logger.info("统计每日挂单平台交易资产 start");
        twdLinkedTradeStatDayService.addTwdLinkedTradeStatDay();
        logger.info("统计每日挂单平台交易资产 end");
    }

	@Override
	public void execute(ShardingContext shardingContext) {
		statisticDailyTradeAsset();
	}

}
