package cc.stbl.token.innerdisc.modules.scheduler;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.entity.VrAiicOperLog;
import cc.stbl.token.innerdisc.modules.basic.service.VrAiicOperLogService;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;

public class AIICPriceConfigScheduler implements SimpleJob {

	// 自动更新为每天5点至23点，随机点更新：每次执行后，计算下次执行时间，定时任务启动时，判断是否已过指定时间，若是，执行业务逻辑
	@Override
	public void execute(ShardingContext shardingContext) {
		logger.debug("AIICPriceConfigScheduler execute start...");
		String[] nextTime = proServive.getStringFromDB(kind, "AIICPriceConfigScheduler_time").split(":");

		LocalTime next = LocalTime.of(Integer.parseInt(nextTime[0]), Integer.parseInt(nextTime[1]),
				Integer.parseInt(nextTime[2]));
		LocalTime current = LocalTime.now();
		if (current.compareTo(next) < 0) {
			logger.debug("AIICPriceConfigScheduler end..未到执行时间 ..");
			return;
		}
		
		VrAiicOperLog currentLog = aiicService.queryCurrentDateLog();
		if (currentLog != null) {
			logger.debug("AIICPriceConfigScheduler end..当天已手动修改过，或定时任务已执行过，本次无需再处理..");
			return; // 当天已手动修改过，或定时任务已执行过，本次无需再处理
		}
		BigDecimal aiicPrice = new BigDecimal(proServive.getStringFromDB(kind, "aiic.price"));
		BigDecimal aiicPriceInc = new BigDecimal(proServive.getStringFromDB(kind, "aiic.price.inc"));
		BigDecimal afterAiicPrice = aiicPrice.add(aiicPriceInc);

		proServive.setProperties(kind, "aiic.price", afterAiicPrice.toString());

		VrAiicOperLog log = new VrAiicOperLog();
		log.setId(JavaUUIDGenerator.get());
		log.setBeforeAiic(aiicPrice);
		log.setAfterAiic(afterAiicPrice);
		log.setOperDate(new Date());
		aiicService.add(log);

		// 设置下次执行时间
		proServive.setProperties(kind, "AIICPriceConfigScheduler_time", genNextTime());
		logger.debug("AIICPriceConfigScheduler execute end...");
	}

	private static String genNextTime() {
		int hour = random.nextInt(23);
		while(hour < 5){
			hour = random.nextInt(23);
		}
		int min = random.nextInt(60);
		int sec = random.nextInt(60);
		return String.format("%s:%s:%s", hour < 10 ? "0" + hour : hour, min < 10 ? "0" + min : min,
				sec < 10 ? "0" + sec : sec);
	}

	private static Random random = new Random();

	@Autowired
	private SysPropertiesService proServive;

	@Autowired
	private VrAiicOperLogService aiicService;

	private static final String kind = "prize";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
