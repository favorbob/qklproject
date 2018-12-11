/**
* generator by mybatis plugin Fri Sep 28 17:36:40 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrAiicOperLog;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface VrAiicOperLogMapper extends IDataStoreMapper<String, VrAiicOperLog> {

	VrAiicOperLog queryCurrentDateLog();
}