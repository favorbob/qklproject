/**
* generator by mybatis plugin Mon Jul 09 16:41:08 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.dao;

import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends IDataStoreMapper<String, SysMenu> {
    List<SysMenu> findByRoles(@Param("kind") String kind,@Param("list") List<String> roleIds);
}