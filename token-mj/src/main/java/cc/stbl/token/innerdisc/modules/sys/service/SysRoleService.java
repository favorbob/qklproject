/**
* generator by mybatis plugin Fri Jul 06 17:05:45 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.sys.dao.SysRoleMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleService extends DataStoreServiceImpl<String, SysRole, SysRoleMapper> {

    @Autowired
    private SysMenuService sysMenuService;

    public List<SysRole> findByUserId(String userId, LoginUser.UserType userTypeem) {
        Integer userType = userTypeem == LoginUser.UserType.ADMIN ? 1 : 2;
        return mapper.findByUserId(userId,userType);
    }

    public List<SysMenu> cacheSysMenuToSession(LoginUser.UserType userType) {
        String userId = ShiroUtils.getLoginUserId();
        UserRolePermission userRolePermission = findUserRolePermission(userId,userType);
        SecurityUtils.getSubject().getSession().setAttribute(UserRolePermission.class.getName(),userRolePermission);
        List<SysMenu> menus = userRolePermission.getSysMenus();
        Map<String,SysMenu> tmp = new HashMap<>();
        for (SysMenu menu : menus) {
           if(!"".equals(menu.getParentId())) tmp.put(menu.getId(),menu);
        }
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu menu : menus) {
            if("".equals(menu.getParentId())) continue;
            if(tmp.containsKey(menu.getParentId())){
                /*if("1".equals(menu.getIsShow()))*/ tmp.get(menu.getParentId()).getChildren().add(menu);
            } else {
                /*if("1".equals(menu.getIsShow()))*/ trees.add(menu);
            }
        }
        return trees;
    }

    public UserRolePermission findUserRolePermission(String userId, LoginUser.UserType userType) {
        List<SysRole> roles = findByUserId(userId, userType);
        List<SysMenu> menus = sysMenuService.findByRoles(roles,getKind(userType));
        return new UserRolePermission(roles,menus);
    }

    private String getKind(LoginUser.UserType userType) {
        return LoginUser.UserType.ADMIN == userType || LoginUser.UserType.VR == userType ? "sys" : "app";
    }
}