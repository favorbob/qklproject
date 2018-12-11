/**
* generator by mybatis plugin Thu Jul 05 16:38:49 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.sys.dao.ActivateCardMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.ActivateCard;

@Service
public class ActivateCardSevice extends DataStoreServiceImpl<String, ActivateCard, ActivateCardMapper> {

	public int insert(ActivateCard activateCard){
		return mapper.insert(activateCard);
	}

}