/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cc.stbl.token.innerdisc.modules.appversion.dao;


import cc.stbl.token.innerdisc.modules.appversion.entity.MallAppVersion;
import com.ks.common.datastore.dao.IDataStoreMapper;

/**
 * App版本DAO接口
 * @author LinJJ
 * @version 2018-04-23
 */
public interface MallAppVersionMapper extends IDataStoreMapper<String,MallAppVersion> {

    MallAppVersion getNewestVersion(String clientType);

    Integer selectMaxVersionCode(String clientType);
}