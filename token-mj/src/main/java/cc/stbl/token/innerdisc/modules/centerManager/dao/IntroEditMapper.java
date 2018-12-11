package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroEdit;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface IntroEditMapper extends IDataStoreMapper<String, IntroEdit> {
    int deleteByPrimaryKey(String id);

    int insert(IntroEdit record);

    int insertSelective(IntroEdit record);

    IntroEdit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IntroEdit record);

    int updateByPrimaryKeyWithBLOBs(IntroEdit record);

    int updateByPrimaryKey(IntroEdit record);

    Long findCount(IntroEdit record); //查询总数
    List<IntroEdit> findList(IntroEdit query, RowBounds rowBounds); //查询集合

    Long findCountIsRead(@Param("user_id")String userId); //根据会员编号，查询已读-未读简介总数
    List<IntroEdit> findListIsRead(@Param("user_id")String userId, Integer offset, Integer pageSize); //根据会员编号，查询已读-未读简介集合

    Long findCountNotRead(@Param("user_id")String userId); //根据会员编号，查询未读简介总数
    List<IntroEdit> findListNotRead(@Param("user_id")String userId, Integer offset, Integer pageSize); //根据会员编号，查询未读简介集合

}