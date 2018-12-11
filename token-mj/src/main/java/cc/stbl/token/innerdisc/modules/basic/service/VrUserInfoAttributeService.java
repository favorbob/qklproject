/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.VrUserInfoAttributeMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfoAttribute;

@Service
public class VrUserInfoAttributeService extends DataStoreServiceImpl<String, VrUserInfoAttribute, VrUserInfoAttributeMapper> {

    

	/**
	 * 通过用户id获取用户基本属性
	 * @param userId
	 * @return
	 */
	public VrUserInfoAttribute getVrUserInfoAttribute(String userId) {
		return mapper.selectByUserId(userId);
	}
	
	/**
	 * 更新
	 * @param entity
	 */
	public void updateVrUserInfoAttribute(VrUserInfoAttribute entity) {
		this.update(entity);
	}
	
	/**
	 * 提交
	 * @param entity
	 */
	public void addVrUserInfoAttribute(VrUserInfoAttribute entity) {
		this.add(entity);
	}
	
	/**
	 * 提交
	 * @param entity
	 */
	public VrUserInfoAttribute selectByUserId(String userId) {
		return mapper.selectByUserId(userId);
	}
   
}