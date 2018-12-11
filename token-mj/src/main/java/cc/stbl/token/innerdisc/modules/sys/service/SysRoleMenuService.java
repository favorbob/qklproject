/**
* generator by mybatis plugin Fri Jul 06 17:06:09 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.modules.sys.dao.SysRoleMenuMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRoleMenu;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuService extends DataStoreServiceImpl<String, SysRoleMenu, SysRoleMenuMapper> {
}