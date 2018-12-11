/**
* generator by mybatis plugin Thu Jul 05 16:38:49 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapter;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.sys.service.SysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTeamLeaderShiroAdapter implements ShiroAdapter{

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Override
    public LoginUser findLoginUser(String loginName) {
        VrUserInfo user = new VrUserInfo();
        user.setPhoneNum(loginName);
        user.setUserLevel(VrUserInfo.USER_LEVEL_TEAMLEADER);
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
        return roleService.findUserRolePermission(userId,LoginUser.UserType.VR);
    }

    @Override
    public LoginUser.UserType listerUserType() {
        return LoginUser.UserType.VR;
    }

//    public SysUser getUserByPhoneNum(String phoneNum) {
//        SysUser query = new SysUser();
//        query.setPhone(phoneNum);
//        return findOne(query);
//    }
}