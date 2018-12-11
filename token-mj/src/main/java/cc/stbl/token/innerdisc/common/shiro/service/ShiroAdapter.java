package cc.stbl.token.innerdisc.common.shiro.service;

public interface ShiroAdapter {
    /**
     *
     * @param findLoginUser 根据登录用户名查找用户
     * @return LoginUser  如果没有用户请返回 null
     */
    LoginUser findLoginUser(String loginName);

    /**
     * 如果访问Controller中的某个方法需要某个权限，在方法上使用 @RequiresPermissions("writeUser")
     * 如果访问Controller中的某个方法需要某个角色, 在方法上使用 @RequiresRoles("admin")
     * @param userId
     * @return 根据userId获取对应的角色集合和权限集合
     */
    UserRolePermission findByUser(String userId);

    LoginUser.UserType listerUserType();
}
