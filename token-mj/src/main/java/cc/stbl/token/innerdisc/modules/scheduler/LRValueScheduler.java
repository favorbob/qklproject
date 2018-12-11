package cc.stbl.token.innerdisc.modules.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLRValueRecordService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLRValueService;

public class LRValueScheduler implements SimpleJob{
	
	@Override
	public void execute(ShardingContext shardingContext) {
		log.debug("LRValueScheduler execute start...");
		
		if(!lock()){
			log.debug("LRValueScheduler 上一个任务还没跑完，本次跳过");
			return;
		}
		
		try {
			String startDate = LocalDate.now().plusDays(-14).toString();
			List<VrUserLRValueRecord> vrUserLRValueRecordList = vrUserLRValueRecordService.selectAllZeroStatus(startDate);
			if(CollectionUtils.isEmpty(vrUserLRValueRecordList)){
				return;
			}
			
			//更新LR
			service.getNeedSetList(startDate).forEach(service::setLRValue);
			
			//更新业绩
			vrUserLRValueRecordList.forEach(vrUserLRValueRecordService::handleLRArea);
		} catch (Exception e) {
			log.error("更新LR值失败：",e);
		}finally {
			unLock();
			log.debug("LRValueScheduler execute end...");
		}
	}
	
	private static synchronized boolean lock(){
		if(!"ok".equals(lockFlag)){
			return false;
		}
		lockFlag = "lock";
		return true;
	}
	
	private static void unLock(){
		lockFlag = "ok";
	}
	
	private static String lockFlag = "ok";
	
	@Autowired
	private VrUserLRValueService service;
	
	@Autowired
    private VrUserLRValueRecordService vrUserLRValueRecordService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

}
