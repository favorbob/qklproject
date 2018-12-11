/**
* generator by mybatis plugin Tue Aug 28 11:37:44 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCount;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface VrUserCountMapper extends IDataStoreMapper<String, VrUserCount> {
    void saveOrUpdate(VrUserCount vrUserCount);
}