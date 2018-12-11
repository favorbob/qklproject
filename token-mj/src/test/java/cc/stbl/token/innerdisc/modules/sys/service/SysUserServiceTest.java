package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SysUserServiceTest extends AbstractTestCase {

    @Autowired
    private SysUserService userService;

    @Test
    public void testRole() throws Exception {
        UserRolePermission permission = userService.findByUser("8a9dd0290f7c4e22bfe8c5fcb1c95fbb");
        System.out.println(permission);
    }

    @Test
    public void createNew() throws Exception {
        SysUser user = new SysUser();
        user.setLoginName("demoAdmin");
        user.setPhone("110");
        user.setPhoto("icon");
        user.setEmail("demoAdmin@bb.cc");
        user.setCompanyId("0");
        user.setName("管理员");
        user.setOfficeId("0");
        user.setPassword("123456");
        userService.createNewUser(user);
    }
}
