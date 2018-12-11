package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticePerson;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface NoticePersonMapper extends IDataStoreMapper<String, NoticePerson> {
    int deleteByPrimaryKey(String id);

    int insert(NoticePerson record);

    int insertSelective(NoticePerson record);

    NoticePerson selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NoticePerson record);

    int updateByPrimaryKey(NoticePerson record);

    NoticePerson selectByNoticeIdUserId(NoticePerson record); //根据NoticeId和UserId 查询是否存在记录
}