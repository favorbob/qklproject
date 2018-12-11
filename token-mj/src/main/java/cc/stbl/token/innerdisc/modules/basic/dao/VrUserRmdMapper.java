/**
* generator by mybatis plugin Wed Jul 18 09:07:54 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VrUserRmdMapper extends IDataStoreMapper<String, VrUserRmd> {
    VrUserRmd findByInviteCodeForupdate(@Param("inviteCode") String inviteCode);

    Integer findDownLine1LevelCount(@Param("userId") String userId);

    List<VrUserRmd> selectByParentUserId(String parentUserId);
}