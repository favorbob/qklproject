package cc.stbl.token.innerdisc.modules.sys.entity;

public class SysUserRole {
    private String id;

    private String userId;

    private String roleId;

    private Integer userType; //用户类型,1:sys_user,2:vr_user

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}