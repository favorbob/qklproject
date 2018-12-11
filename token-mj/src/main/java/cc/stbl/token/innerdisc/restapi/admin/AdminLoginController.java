package cc.stbl.token.innerdisc.restapi.admin;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.service.SysRoleService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = {PathPrefix.ADMIN_PATH })
@RestController
@Api(description = "登录-管理后台")
public class AdminLoginController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Response login(@RequestBody @Valid LoginRequest request){
        try {
            Subject subject = SecurityUtils.getSubject();
            UserPwdToken token = new UserPwdToken();
            token.setUsername(request.getLoginName());
            token.setPassword(request.getPassword().toCharArray());
            LoginUser.UserType userType= StringUtils.isEmpty(request.getUserType())?
                    LoginUser.UserType.ADMIN:LoginUser.UserType.valueOf(request.getUserType());
            token.setUserType(userType);
            subject.login(token);
//            sysUserService.findByUser(ShiroUtils.getLoginUserId());
            List<SysMenu> menuList = sysRoleService.cacheSysMenuToSession(token.getUserType());
            return Response.success(LoginSuccess.success(subject.getSession().getId().toString(),menuList));
        } catch (CredentialsException e) {
            return Response.bulid(ResponseCode.LOGIN_PASSWD_ERROR);
        } catch (AuthenticationException e) {
            Throwable cases = e.getCause();
            while (cases != null) {
                if(cases instanceof StructWithCodeException) {
                    return ((StructWithCodeException)cases).toResponse();
                }
                cases = cases.getCause();
            }
            e.printStackTrace();
            return Response.bulid(ResponseCode.ERROR);
        }
    }

    @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    @ApiOperation("登出-管理后台")
    public Response logOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.success(null);
    }

    public static class LoginSuccess{
        private String token;
        private List<SysMenu> menuList;
        public static LoginSuccess success(String accessToken,List<SysMenu> menuList){
            LoginSuccess s = new LoginSuccess();
            s.setToken(accessToken);
            s.setMenuList(menuList);
            return s;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public List<SysMenu> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<SysMenu> menuList) {
            this.menuList = menuList;
        }
    }

    public static class LoginRequest{
        @NotBlank(message = "请输入登录名")
        String loginName;
        @NotBlank(message = "请输入密码")
        String password;
        @ApiModelProperty("用户类型：ADMIN，VR")
        String userType;
        public String getLoginName() {
            return loginName;
        }
        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getUserType() {
            return userType;
        }
        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
