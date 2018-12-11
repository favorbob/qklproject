package cc.stbl.token.innerdisc.common.shiro.authc;

//import org.crazycake.shiro.AuthCachePrincipal;

import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;

public class UserPrincipal implements AuthCachePrincipal,Serializable {

    private String userId;

    private String loginName;

    private String userType; // 1 app : 2:pc

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public LoginUser.UserType toUserType(){
        return LoginUser.UserType.valueOf(this.userType);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthCacheKey() {
        return getUserId();
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
