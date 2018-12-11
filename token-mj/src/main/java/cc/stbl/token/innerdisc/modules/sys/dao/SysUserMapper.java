/**
* generator by mybatis plugin Tue Jul 10 09:42:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.dao;

import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SysUserMapper extends IDataStoreMapper<String, SysUser> {
    Long findCountByName(String name);   //根据姓名模糊查询总数
    List<SysUser> findListByName(String name, RowBounds rowBounds); //根据姓名模糊查询列表
}