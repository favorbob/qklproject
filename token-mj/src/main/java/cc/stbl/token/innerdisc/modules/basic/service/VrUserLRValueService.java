/**
* generator by mybatis plugin Wed Oct 10 16:10:20 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.VrUserLRValueMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;

@Service
public class VrUserLRValueService extends DataStoreServiceImpl<String, VrUserLRValue, VrUserLRValueMapper> {

	/**
	 * @param userId
	 * @param level
	 * @param status 用户状态列表，若不指定则返回所有状态的用户
	 * @return 返回往下level层的用户列表 加上自己在列表头
	 */
	public List<VrUserLRValue> getSubList(String userId, int level,int... status) {
		VrUserLRValue lr = get(userId);
		return getSubList(lr,level,status);
	}
	
	/**
	 * @param userId
	 * @param level 0 表示返回所有上级用户列表
	 * @param status 用户状态列表，若不指定则返回所有状态的用户
	 * @return 返回level层上级用户列表
	 */
	public List<VrUserLRValue> getParentList(String userId, int level,int... status) {
		if(StringUtils.isBlank(userId) || level < 0){
			return new ArrayList<>();
		}
		VrUserLRValue lr = get(userId);
		lr.setUserLevel(level);
		return mapper.getParentList(lr,status);
	}
	
	/**
	 * @param userId
	 * @param status 用户状态列表，若不指定则返回所有状态的用户
	 * @return 返回所有上级用户列表
	 */
	public List<VrUserLRValue> getAllParentList(String userId,int... status) {
		return getParentList(userId,0,status);
	}

	/**
	 * @param lr
	 * @param level
	 * @param status 用户状态列表，若不指定则返回所有状态的用户
	 * @return 返回往下level层的用户列表 加上自己在列表头
	 */
	public List<VrUserLRValue> getSubList(VrUserLRValue lr, int level,int... status) {
		if(lr == null || level < 0){
			return new ArrayList<>();
		}
		lr.setUserLevel(lr.getUserLevel() + level);
		return mapper.getSubList(lr,status);
	}

	/**
	 * @return 需要设置左右值的用户列表
	 */
	public List<VrUserLRValue> getNeedSetList(String startDate) {
		return mapper.getNeedSetList(startDate);
	}

	/**
	 * 设置lr值。一级用户需要手工在数据库生成
	 * @param lrValue 新增用户，待设置lr值
	 */
	public void setLRValue(VrUserLRValue lrValue) {
		if(lrValue == null || "0".equals(lrValue.getPid()) || lrValue.getLft() !=-1){
			return;
		}
		// 查询是否已经有同级成员
		VrUserLRValue other = mapper.findOther(lrValue);
		VrUserLRValue parent = get(lrValue.getPid());
		String treeId = parent.getTreeId();
		if (other == null) {
			int pLft = parent.getLft();
			mapper.setLValue(treeId,pLft);
			mapper.setRValue(treeId,pLft);

			lrValue.setLft(pLft + 1);
			lrValue.setRgt(pLft + 2);
		} else {
			if ("A".equals(other.getArea())) {
				int oRgt = other.getRgt();
				mapper.setLValue(treeId,oRgt);
				mapper.setRValue(treeId,oRgt);

				lrValue.setLft(oRgt + 1);
				lrValue.setRgt(oRgt + 2);
			} else {
				int oLft = other.getLft();
				mapper.setLValue(treeId,oLft - 1);
				mapper.setRValue(treeId,oLft);

				lrValue.setLft(oLft);
				lrValue.setRgt(oLft + 1);
			}
		}
		lrValue.setUserLevel(parent.getUserLevel() + 1);
		lrValue.setTreeId(treeId);
		update(lrValue);
	}

	public List<VrUserLRValue> getPrizeUserList(int minLevel, String localDate) {
		return mapper.getPrizeUserList(minLevel, localDate);
	}
	
	/**
	 * 根据pid和区域查询实体
	 * @param pid
	 * @param area
	 * @return
	 */
	public VrUserLRValue selectByPidAndArea(String pid,String area) {
		return mapper.selectByPidAndArea(pid, area);
	}
	
	
	/*public void batch(){
		for (int j = 0; j < 340; j++) {
			List<VrUserLRValue> list = new ArrayList<>(300);
			for(int i=1;i< 301;i++){
				VrUserLRValue lr = new VrUserLRValue();
				lr.setUserId(UUID.randomUUID().toString());
				lr.setPid(UUID.randomUUID().toString());
				lr.setLft(j * 300 +i);
				lr.setRgt(j * 300 +i +1);
				list.add(lr);
			}
			mapper.batchInsertSelective(list);
		}
	}
	
	public void set(){
		mapper.setLValue("1", 1);
		mapper.setRValue("1", 1);
	}*/

}