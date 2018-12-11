/**
* generator by mybatis plugin Wed Oct 10 16:10:20 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;

public interface VrUserLRValueRecordMapper extends IDataStoreMapper<String, VrUserLRValueRecord> {

	/*
	 * 查询所有状态为0的实体
	 */
	public List<VrUserLRValueRecord> selectAllZeroStatus(String startDate);
	
	/*
	 * 更加userid查询业绩记录对象
	 */
	public VrUserLRValueRecord selectByUserId(@Param("userId") String userId);
	
}