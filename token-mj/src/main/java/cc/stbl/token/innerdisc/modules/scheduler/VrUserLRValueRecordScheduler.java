package cc.stbl.token.innerdisc.modules.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cogent.cache.CacheService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLRValueRecordService;

/**
 * 业绩定时器
 * @author fyf
 *
 */
//@Component
public class VrUserLRValueRecordScheduler implements SimpleJob{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CacheService cache;
	private static String lock = "vrUserLRValueRecordLock";
	
    @Autowired
    private VrUserLRValueRecordService vrUserLRValueRecordService;

    @Override
    public void execute(ShardingContext shardingContext) {
    	/*logger.info("VrUserLRValueRecordScheduler start");
    	int flag = cache.setNx(lock, LocalDate.now().toString());
    	if(flag == 0) {
    		logger.info("VrUserLRValueRecordScheduler 还有任务没处理完");
    		return ;
    	}
    	
		try {
	    	List<VrUserLRValueRecord> vrUserLRValueRecordList = vrUserLRValueRecordService.selectAllZeroStatus();
	    	for(VrUserLRValueRecord v:vrUserLRValueRecordList) {
	    		vrUserLRValueRecordService.handleLRArea(v);
	    	}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		cache.remove(lock);
    	logger.info("VrUserLRValueRecordScheduler end");*/
    }
}
