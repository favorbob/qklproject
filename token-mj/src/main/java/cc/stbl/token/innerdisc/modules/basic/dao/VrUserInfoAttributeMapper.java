/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfoAttribute;

import com.ks.common.datastore.dao.IDataStoreMapper;


public interface VrUserInfoAttributeMapper extends IDataStoreMapper<String, VrUserInfoAttribute> {
  
    
    /**
     * 通过userid查询用户属性表
     * @param userId
     * @return
     */
    VrUserInfoAttribute selectByUserId(String userId);
}