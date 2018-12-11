package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserAccountMaintain;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface VrUserAccountMaintainMapper  extends IDataStoreMapper<String, VrUserAccountMaintain> {
    int deleteByPrimaryKey(String id);

    int insert(VrUserAccountMaintain record);

    int insertSelective(VrUserAccountMaintain record);

    VrUserAccountMaintain selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(VrUserAccountMaintain record);

    int updateByPrimaryKey(VrUserAccountMaintain record);
}