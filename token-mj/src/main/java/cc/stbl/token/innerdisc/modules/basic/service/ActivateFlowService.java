/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.ActivateCardFlowMapper;
import cc.stbl.token.innerdisc.modules.basic.dao.ActivateFlowMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateFlow;

@Service
public class ActivateFlowService extends DataStoreServiceImpl<String, ActivateFlow, ActivateFlowMapper> {

	
	public ActivateFlow add(ActivateFlow activateFlow) {
		mapper.insertSelective(activateFlow);
		return activateFlow;
	}
	
	public List<ActivateFlow> findAllByUserId(String userId) {
		return mapper.findAllByUserId(userId);
	}
}