/**
* generator by mybatis plugin Fri Sep 28 17:36:40 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.VrAiicOperLogMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrAiicOperLog;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VrAiicOperLogService extends DataStoreServiceImpl<String, VrAiicOperLog, VrAiicOperLogMapper> {

	public VrAiicOperLog queryCurrentDateLog() {
		return mapper.queryCurrentDateLog();
	}
}