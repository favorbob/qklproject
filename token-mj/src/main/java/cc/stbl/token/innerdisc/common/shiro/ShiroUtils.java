package cc.stbl.token.innerdisc.common.shiro;

import cc.stbl.token.innerdisc.common.shiro.authc.UserPrincipal;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


public abstract class ShiroUtils {

    public static String getLoginUserId(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.getSession(false) == null) return null;
        UserPrincipal userPrincipal = (UserPrincipal)subject.getPrincipals().getPrimaryPrincipal();
        return userPrincipal == null ? null : userPrincipal.getUserId();
    }

    public static LoginUser.UserType  getLoginUserType(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.getSession(false) == null) return null;
        UserPrincipal userPrincipal = (UserPrincipal)subject.getPrincipals().getPrimaryPrincipal();
        return userPrincipal == null ? null : LoginUser.UserType.valueOf(userPrincipal.getUserType());
    }

    public static String  getLoginUserName(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.getSession(false) == null) return null;
        UserPrincipal userPrincipal = (UserPrincipal)subject.getPrincipals().getPrimaryPrincipal();
        return userPrincipal == null ? null : userPrincipal.getLoginName();
    }

}
