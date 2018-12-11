package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroPerson;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface IntroPersonMapper extends IDataStoreMapper<String, IntroPerson> {
    int deleteByPrimaryKey(String id);

    int insert(IntroPerson record);

    int insertSelective(IntroPerson record);

    IntroPerson selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IntroPerson record);

    int updateByPrimaryKey(IntroPerson record);

    IntroPerson selectByIntroIdUserId(IntroPerson record); //根据introId和UserId 查询是否存在记录
}