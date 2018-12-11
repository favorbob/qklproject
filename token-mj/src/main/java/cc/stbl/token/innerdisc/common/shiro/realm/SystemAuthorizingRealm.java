package cc.stbl.token.innerdisc.common.shiro.realm;

import cc.stbl.token.innerdisc.common.shiro.authc.UserPrincipal;
import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapterProxy;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private ShiroAdapterProxy shiroAdapterProxy;

    /**
     * 获取权限授权信息, 登录成功后调用
     *  // How to use it    @RequiresPermissions("wrapStr") @RequiresRoles("admin")
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserPrincipal principal = (UserPrincipal)principalCollection.getPrimaryPrincipal();
        UserRolePermission rolePermission = shiroAdapterProxy.findByUser(principal.getUserId(),principal.toUserType());
        if(rolePermission == null) rolePermission = new UserRolePermission(null,null);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(rolePermission.getRoles());
        authorizationInfo.addStringPermissions(rolePermission.getPermissions());
        return authorizationInfo;
    }

    /**
     *  登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserPwdToken upt = (UserPwdToken)authenticationToken;
        UserPrincipal principal = new UserPrincipal();
        String username = upt.getUsername();
        LoginUser loginUser = shiroAdapterProxy.findLoginUser(upt);
//        if(upt.getCaptcha() == null) throw new AuthenticationException("认证码错误");
        if(loginUser == null) throw new StructWithCodeException(ResponseCode.LOGIN_USER_NOT_FOUND_ERROR);
        if(loginUser.getStatus() == VrUserInfo.USER_STATUS_FREEZE) throw new StructWithCodeException(ResponseCode.LOGIN_USER_LOCK_ERROR);
        // 认证密码等
        char[] passwd = loginUser.getPassword().toCharArray();
        principal.setLoginName(username);
        principal.setUserId(loginUser.getUserId());
        principal.setUserType(loginUser.getUserType().name());
        return new SimpleAuthenticationInfo(principal,passwd,ByteSource.Util.bytes(loginUser.getSalt()),getName());
    }
}
