package cc.stbl.token.innerdisc.common.shiro.service;

import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRolePermission implements Serializable{

    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    private final List<SysMenu> sysMenus;

    public UserRolePermission(List<SysRole> roles, List<SysMenu> menus){
        this.sysMenus = menus;
        if(CollectionUtils.isNotEmpty(roles)){
            for (SysRole role : roles) {
                if(StringUtils.isNotBlank(role.getRoleType())) this.roles.add(role.getRoleType());
            }
        }
        if(CollectionUtils.isNotEmpty(menus)){
            for (SysMenu menu : menus) {
                if(StringUtils.isNotBlank(menu.getPermission())) {
                     String[] permarray = menu.getPermission().split(",");
                    for (String p : permarray) {
                        this.permissions.add(p);
                    }
                }
            }
        }
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }
}
