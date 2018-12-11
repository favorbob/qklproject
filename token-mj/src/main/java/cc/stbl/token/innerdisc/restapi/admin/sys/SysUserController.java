package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.sys.entity.SysUser;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.Auth;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/sysUser")
//@RequiresRoles({Auth.ROLE_ADMIN})
@Api(description = "系统用户管理")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    @ApiOperation("获取系统用户信息")
    public Response<Pager<SysUser>> list(@RequestBody SysUserProto.ListRequest request){
        String userId = ShiroUtils.getLoginUserId();
        if("".equals(userId.trim())) {
            Response.error("未登陆系统，不能进行操作！");
        }
        SysUser query = new SysUser();
        BeanUtils.copyProperties(request,query);
        Pager<SysUser> pager = sysUserService.findPage(query,request.getPageNo(),request.getPageSize());
        pager.getList().forEach(u -> {
            u.setPassword(null);
            u.setSalt(null);
        });
        return Response.success(pager);
    }
    /*
    @RequestMapping(value = {"/create"},method = RequestMethod.POST)
    @RequiresRoles({Auth.ROLE_ADMIN,Auth.ROLE_TEAM_LEADER})
    public Response<String> createUser(@RequestBody SysUserProto.CreateUserRequest request){
        SysUser sysUser = new SysUser();
*//*        sysUser.setUserLevel(request.getUserLevel());
        sysUser.setPassword(request.getPassword());
        sysUser.setPhoneNum(request.getPhoneNum());
        sysUser.setInviteCode(ShiroUtils.getLoginUserId());*//*
        sysUserService.createNewUser(sysUser);
        return Response.success(sysUser.getUserId());
    }*/

    @RequestMapping(value = {"/updatePassword"},method = RequestMethod.POST)
    public Response<String> updatePassword(@RequestBody @Valid SysUserProto.UpdatePasswordRequest request){
        sysUserService.updatePassword(ShiroUtils.getLoginUserId(),request.getOldPassword(),request.getNewPassword());
        return Response.success("更新成功");
    }

    @RequestMapping(value = {"/getSysUserInfo"},method = RequestMethod.POST)
    @ApiOperation("首页用户信息")
    public Response<SysUserProto.ResponseGetSysUserInfo> getSysUserInfo(){
        return Response.success(sysUserService.getSysUserInfo(ShiroUtils.getLoginUserId()));
    }
    @RequestMapping(value = {"/updateSysUserInfo"},method = RequestMethod.POST)
    @ApiOperation("更新系统用户信息")
    public Response<String> updateSysUserInfo(@RequestBody @Valid SysUserProto.RequestSysUserInfo request){
        String userId = ShiroUtils.getLoginUserId();
        SysUser object = new SysUser();
        BeanUtils.copyProperties(request, object);
        sysUserService.update(object);
        return Response.success("更新系统用户信息成功！");
    }

}
