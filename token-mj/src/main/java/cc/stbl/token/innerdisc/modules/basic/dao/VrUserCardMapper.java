/**
* generator by mybatis plugin Tue Aug 28 11:37:44 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ks.common.datastore.dao.IDataStoreMapper;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;

public interface VrUserCardMapper extends IDataStoreMapper<String, VrUserCard> {
    
	public List<Map<String,Object>> groupByCardTypeAndStatus();
	
	public void updateByIds(@Param("status")Integer status,@Param("list")List<String> list);
	
	public Integer countByUserIdAndStatus(@Param("userId")String userId);
	
	/**
	 * 查询所有
	 * @param userId
	 * @return
	 */
	public List<VrUserCard> selectByUserIdAndStatus(@Param("userId")String userId);
	/**
	 * 查询5条
	 * @param userId
	 * @return
	 */
	public List<VrUserCard> findByUserIdAndStatus(@Param("userId")String userId);
	
	public void updateCardByIds(@Param("list")List<String> list,@Param("userId")String userId,@Param("phoneNum")String phoneNum,@Param("updateTime")Date updateTime);
}