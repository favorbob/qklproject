/**
* generator by mybatis plugin Fri Jul 06 17:05:45 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.dao;

import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends IDataStoreMapper<String, SysRole> {
    List<SysRole> findByUserId(@Param("userId") String userId,@Param("userType")  Integer userType);
}