package cc.stbl.token.innerdisc.common.shiro.service;

public class LoginUser {
    private String password;
    private String username;
    private Integer status;
    private String userId;
    private String salt;
    private UserType userType = UserType.ADMIN;


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public enum UserType {
        API, ADMIN, VR, APP_MOCK,ADMIN_MOCK;
        public boolean isAdminSysLogin() {
            return this == ADMIN || this == VR || this == ADMIN_MOCK;
        }
        public boolean isAppLogin(){
            return !isAdminSysLogin();
        }
    }
}
