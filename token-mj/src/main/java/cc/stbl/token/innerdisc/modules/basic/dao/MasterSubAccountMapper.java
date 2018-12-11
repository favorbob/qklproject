/**
* generator by mybatis plugin Wed Jul 18 09:07:54 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.modules.basic.entity.MasterSubAccount;

public interface MasterSubAccountMapper extends IDataStoreMapper<String, MasterSubAccount> {
    
	public List<MasterSubAccount> findListBySubPhoneNum(@Param("subPhoneNum")String subPhoneNum);

	public MasterSubAccount findBySubPhoneNum(@Param("subPhoneNum")String subPhoneNum);
}