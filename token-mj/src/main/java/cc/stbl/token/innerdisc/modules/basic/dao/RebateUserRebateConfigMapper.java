/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserRebateConfig;
import com.ks.common.datastore.dao.IDataStoreMapper;

import java.util.List;

public interface RebateUserRebateConfigMapper extends IDataStoreMapper<String, RebateUserRebateConfig> {
    void batchAddOrUpdate(List<RebateUserRebateConfig> batchList);
}