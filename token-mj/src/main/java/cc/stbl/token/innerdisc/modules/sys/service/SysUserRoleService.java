/**
* generator by mybatis plugin Wed Jul 11 15:00:16 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.sys.dao.SysUserRoleMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUserRole;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleService extends DataStoreServiceImpl<String, SysUserRole, SysUserRoleMapper> {
    public SysUserRole create(String userId,String roleId,int userType){
        SysUserRole userRole=new SysUserRole();
        userRole.setId(JavaUUIDGenerator.get());
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setUserType(userType);
        super.add(userRole);
        return userRole;
    }
}