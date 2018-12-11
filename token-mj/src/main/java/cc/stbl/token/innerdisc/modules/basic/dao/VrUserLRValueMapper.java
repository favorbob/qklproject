/**
* generator by mybatis plugin Wed Oct 10 16:10:20 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;

public interface VrUserLRValueMapper extends IDataStoreMapper<String, VrUserLRValue> {
	VrUserLRValue findOther(VrUserLRValue lrValue);

	void setLValue(@Param("treeId") String treeId, @Param("val") int val);

	void setRValue(@Param("treeId") String treeId, @Param("val") int val);

	List<VrUserLRValue> getNeedSetList(String startDate);

	List<VrUserLRValue> getSubList(@Param("lr")VrUserLRValue lr,@Param("status")int... status);

	List<VrUserLRValue> getPrizeUserList(@Param("minLevel") int minLevel, @Param("createDate") String createDate);

	List<VrUserLRValue> getParentList(@Param("lr")VrUserLRValue lr,@Param("status")int... status);
	
	public VrUserLRValue selectByPidAndArea(@Param("pid")String pid,@Param("area")String area);
	
	
}