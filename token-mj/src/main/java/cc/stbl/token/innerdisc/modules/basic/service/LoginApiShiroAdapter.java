package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapter;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.sys.service.SysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginApiShiroAdapter implements ShiroAdapter{

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Override
    public LoginUser findLoginUser(String loginName) {
        VrUserInfo user = new VrUserInfo();
        user.setPhoneNum(loginName);
        user = vrUserInfoService.findOne(user);
        if(user == null || user.getStatus()== -1) return null;
        LoginUser loginUser = new LoginUser();
        loginUser.setPassword(user.getPassword());
        loginUser.setSalt(user.getSalt());
        loginUser.setStatus(user.getStatus());
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(listerUserType());
        return loginUser;
    }

    @Override
    public UserRolePermission findByUser(String userId) {
        Session session = SecurityUtils.getSubject().getSession(false);
        if(session != null){
            UserRolePermission ur = (UserRolePermission)session.getAttribute(UserRolePermission.class.getName());
            if(ur!= null) return ur;
        }
        return roleService.findUserRolePermission(userId,LoginUser.UserType.API);
    }

    @Override
    public LoginUser.UserType listerUserType() {
        return LoginUser.UserType.API;
    }
}
