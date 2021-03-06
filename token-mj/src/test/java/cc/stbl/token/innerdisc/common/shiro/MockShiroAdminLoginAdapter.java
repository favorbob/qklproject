package cc.stbl.token.innerdisc.common.shiro;

import cc.stbl.token.innerdisc.common.shiro.authc.PasswordEncoder;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapter;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import cc.stbl.token.innerdisc.modules.sys.service.SysRoleService;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockShiroAdminLoginAdapter implements ShiroAdapter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysRoleService roleService;

    @Override
    public LoginUser findLoginUser(String loginName) {
        SysUser user = new SysUser();
        user.setLoginName(loginName);
        user = sysUserService.findOne(user);
        if(user == null || user.getStatus()== -1) return null;
        LoginUser loginUser = new LoginUser();
        loginUser.setPassword(passwordEncoder.encoder("123456","salt"));
        loginUser.setSalt("salt");
        loginUser.setStatus(user.getStatus());
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getLoginName());
        loginUser.setUserType(listerUserType());
        return loginUser;
    }

    public UserRolePermission findByUser(String userId) {
        Session session = SecurityUtils.getSubject().getSession(false);
        if(session != null){
            UserRolePermission ur = (UserRolePermission)session.getAttribute(UserRolePermission.class.getName());
            if(ur!= null) return ur;
        }
        return roleService.findUserRolePermission(userId,LoginUser.UserType.ADMIN);
    }

    @Override
    public LoginUser.UserType listerUserType() {
        return LoginUser.UserType.ADMIN_MOCK;
    }
}
