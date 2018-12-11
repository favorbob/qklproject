package cc.stbl.token.innerdisc.common.shiro.authc;

import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UserPwdToken extends UsernamePasswordToken {

    private String captcha;

    private LoginUser.UserType userType;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public LoginUser.UserType getUserType() {
        return userType;
    }

    public void setUserType(LoginUser.UserType userType) {
        this.userType = userType;
    }
}
