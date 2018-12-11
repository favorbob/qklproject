/**
* generator by mybatis plugin Wed Jul 18 09:07:54 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserRmdMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VrUserRmdService extends DataStoreServiceImpl<String, VrUserRmd, VrUserRmdMapper> {
    @Autowired
    private VrUserInfoService vrUserInfoService;

    public VrUserRmd createSeedUserRmd(VrUserInfo userInfo){
        VrUserRmd seedUserRmd=new VrUserRmd();
        seedUserRmd.setId(JavaUUIDGenerator.get());
        seedUserRmd.setSeedUserId(userInfo.getUserId());
        seedUserRmd.setUserId(userInfo.getUserId());
        seedUserRmd.setUserLevel(userInfo.getUserLevel());
        seedUserRmd.setRegisterDate(userInfo.getCreateDate());
        seedUserRmd.setRmdLevel(1);
        seedUserRmd.setSubordinates(0);
        seedUserRmd.setInviteCode(userInfo.getInviteCode());
        super.add(seedUserRmd);
        return seedUserRmd;
    }

    public VrUserRmd createSubUserRmd(VrUserInfo userInfo,String parentInviteCode){
        VrUserInfo parent = vrUserInfoService.getUserByInviteCode(parentInviteCode);
        if (parent == null || parent.getStatus() != VrUserInfo.USER_STATUS_NORMAL) {
            throw new StructWithCodeException(ResponseCode.USER_RMD_PARTNER_NOT_FOUND);
        }
        VrUserRmd parentRmd = mapper.findByInviteCodeForupdate(parentInviteCode);
        VrUserRmd vrUserRmd = new VrUserRmd();
        vrUserRmd.setId(JavaUUIDGenerator.get());
        vrUserRmd.setUserId(userInfo.getUserId());
        vrUserRmd.setParentUserId(parentRmd.getUserId());
        vrUserRmd.setRegisterDate(userInfo.getCreateDate());
        vrUserRmd.setUserLevel(userInfo.getUserLevel());
        vrUserRmd.setInviteCode(userInfo.getInviteCode());
        vrUserRmd.setSeedUserId(parentRmd.getSeedUserId());
        vrUserRmd.setRmdLevel(parentRmd.getRmdLevel() + 1);
        vrUserRmd.setSubordinates(0);
        vrUserRmd.setParentInviteCode(parentInviteCode);
        super.add(vrUserRmd);
        parentRmd.setSubordinates(parentRmd.getSubordinates() + 1);
        super.update(parentRmd);
        return vrUserRmd;
    }

    public VrUserRmd createVrUserRmd(VrUserInfo vrUserInfo,String parentInviteCode){
        if(StringUtils.isNotBlank(parentInviteCode)) {
            return createSubUserRmd(vrUserInfo,parentInviteCode);
        } else  {
           return createSeedUserRmd(vrUserInfo);
        }
    }

    public boolean checkMembership(String pUserId,String sUserId){
        VrUserRmd sql=new VrUserRmd();
        sql.setParentUserId(pUserId);
        sql.setUserId(sUserId);
        return super.findOne(sql)==null ? false:true;
    }

    public Integer findDownLine1LevelCount(String userId){
        return mapper.findDownLine1LevelCount(userId);
    }

    public List<VrUserRmd> selectByParentUserId(String parentUserId){
        return mapper.selectByParentUserId(parentUserId);
    }
}