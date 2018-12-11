package cc.stbl.token.innerdisc.modules.scheduler;

import cc.stbl.token.innerdisc.modules.basic.service.ResourceUserUsedTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

//@Component
public class UserUsedTimeScheduler {

    private static Logger logger = LoggerFactory.getLogger(UserUsedTimeScheduler.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ResourceUserUsedTimeService userUsedTimeService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void autoEndCountUserUsedTime(){
        Long time = 5 * 1000L;
        Long flag = System.currentTimeMillis() - time;
        Set<String> set = stringRedisTemplate.opsForZSet().rangeByScore(userUsedTimeService.TIME_KEY, 0, flag);
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String id = it.next();
            userUsedTimeService.countUsedTimeEnd(id);
        }
    }

}
