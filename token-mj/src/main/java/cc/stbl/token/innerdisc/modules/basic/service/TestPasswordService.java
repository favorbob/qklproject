/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.TestPasswordMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.TestPassword;

@Service
public class TestPasswordService extends DataStoreServiceImpl<String, TestPassword, TestPasswordMapper> {

    
   
	public void addTestPassword(TestPassword entity) {
		mapper.insertSelective(entity);
	}
   
}