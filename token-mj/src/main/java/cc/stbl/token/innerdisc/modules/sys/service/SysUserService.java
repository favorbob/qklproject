/**
* generator by mybatis plugin Thu Jul 05 16:38:49 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.sys.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.UserTypeEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.authc.PasswordEncoder;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapter;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.modules.sys.dao.SysUserMapper;
import cc.stbl.token.innerdisc.modules.sys.entity.SysMenu;
import cc.stbl.token.innerdisc.modules.sys.entity.SysRole;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.Auth;
import cc.stbl.token.innerdisc.restapi.admin.sys.SysUserProto;
import com.google.common.collect.Sets;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysUserService extends DataStoreServiceImpl<String, SysUser, SysUserMapper> implements ShiroAdapter{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysRoleService roleService;

    public SysUser createNewUser(SysUser sysUser) {
        String plainPwd = sysUser.getPassword();
        sysUser.setSalt(JavaUUIDGenerator.get());
        sysUser.setPassword(passwordEncoder.encoder(plainPwd,sysUser.getSalt()));
        sysUser.setCreateDate(new Date());
        sysUser.setUpdateDate(new Date());
        sysUser.setUserId(JavaUUIDGenerator.get());
        return super.add(sysUser);
    }

    public void updatePassword(String userId,String oldPassword,String newPassword){
        SysUser user = get(userId);
        if(!passwordEncoder.encoder(oldPassword,user.getSalt()).equals(user.getPassword())) throw new StructWithCodeException(ResponseCode.LOGIN_PASSWD_ERROR);
        user.setPassword(passwordEncoder.encoder(newPassword,user.getSalt()));
        user.setUpdateDate(new Date());
        update(user);
    }

    @Override
    public LoginUser findLoginUser(String loginUserName) {
        SysUser user = new SysUser();
        user.setLoginName(loginUserName);
        user = findOne(user);
        if(user == null) return null;
        LoginUser loginUser = new LoginUser();
        loginUser.setPassword(user.getPassword());
        loginUser.setSalt(user.getSalt());
        loginUser.setStatus(user.getStatus());
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getLoginName());
        loginUser.setUserType(listerUserType());
        return loginUser;
    }

	@Override
    public UserRolePermission findByUser(String userId) {
        Session session = SecurityUtils.getSubject().getSession(false);
        if(session != null){
            UserRolePermission ur = (UserRolePermission)session.getAttribute(UserRolePermission.class.getName());
            if(ur!= null) return ur;
        }
        return roleService.findUserRolePermission(userId,LoginUser.UserType.ADMIN);
    }

    @Override
    public LoginUser.UserType listerUserType() {
        return LoginUser.UserType.ADMIN;
    }

    public SysUser getUserByPhoneNum(String phoneNum) {
        SysUser query = new SysUser();
        query.setPhone(phoneNum);
        return findOne(query);
    }

    public SysUserProto.ResponseGetSysUserInfo getSysUserInfo(String userId){
        SysUser sysUser = super.get(userId);
        if (sysUser == null){
            throw new StructWithCodeException(ResponseCode.LOGIN_NO);
        }
        SysUserProto.ResponseGetSysUserInfo info = new SysUserProto.ResponseGetSysUserInfo();
        BeanUtils.copyProperties(sysUser, info);
        return info;
    }

    /**
     * 根据姓名模糊查询分页数据
     * @param uname
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<SysUser> findSysUserListByName(String uname, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountByName(uname);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<SysUser>(), total);
        }
        List<SysUser> list = this.mapper.findListByName(uname,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }


}