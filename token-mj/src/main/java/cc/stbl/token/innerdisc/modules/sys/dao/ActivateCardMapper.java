/**
* generator by mybatis plugin Mon Jul 09 16:41:08 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.dao;

import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.modules.sys.entity.ActivateCard;

public interface ActivateCardMapper extends IDataStoreMapper<String, ActivateCard> {
	int insert(ActivateCard activateCard);
}