/**
* generator by mybatis plugin Mon Jul 09 16:41:08 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.modules.sys.dao.SysMenuMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuService extends DataStoreServiceImpl<String, SysMenu, SysMenuMapper> {

    public List<SysMenu> findByRoles(List<SysRole> roles,String kind) {
        if(roles == null || roles.size() == 0) return new ArrayList<>();
        List<String> roleIds = new ArrayList<>();
        for (SysRole role : roles) {
            if("sysAdmin".equals(role.getRoleType())) return findByKind(kind);
            roleIds.add(role.getId());
        }
        return mapper.findByRoles(kind,roleIds);
    }

    public List<SysMenu> findByKind(String kind) {
        SysMenu q = new SysMenu();
        q.setKind(kind);
        return findList(q);
    }
}