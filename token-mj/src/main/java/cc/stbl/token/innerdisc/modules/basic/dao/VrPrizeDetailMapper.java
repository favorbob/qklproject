/**
* generator by mybatis plugin Thu Sep 27 10:50:19 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.session.RowBounds;

public interface VrPrizeDetailMapper extends IDataStoreMapper<String, VrPrizeDetail> {

	/**
	 * 查询当天是否有红包领取
	 * @param userId
	 * @return
	 */
	VrPrizeDetail selectByUserIdAndTime(@Param("userId")String userId,@Param("createTime")String createTime);

	/**
	 * 根据userid统计领取红包次数
	 * @param userId
	 * @return
	 */
	int countByUserId(String userId);
	
	/**
	 * 根据userid统计mj和aiic
	 * @param userId
	 * @return
	 */
	Map<String,BigDecimal> countMjAndAiicByUserId(String userId);

	Long findVrPrizeDetailCount(VrPrizeDetail query); //查询奖金明细总数
	List<VrPrizeDetail> findVrPrizeDetailList(VrPrizeDetail query, RowBounds rowBounds); //查询奖金明细集合
}