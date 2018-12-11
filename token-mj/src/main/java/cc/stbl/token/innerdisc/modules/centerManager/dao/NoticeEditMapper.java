package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEdit;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface NoticeEditMapper extends IDataStoreMapper<String, NoticeEdit> {
    int deleteByPrimaryKey(String id);

    int insert(NoticeEdit record);

    int insertSelective(NoticeEdit record);

    NoticeEdit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NoticeEdit record);

    int updateByPrimaryKeyWithBLOBs(NoticeEdit record);

    int updateByPrimaryKey(NoticeEdit record);

    Long findUserNoticeCount(@Param("user_id")String userId); //根据会员编号，查询个人消息总数
    List<NoticeEdit> findUserNoticeList(@Param("user_id")String userId, Integer offset, Integer pageSize); //根据会员编号，查询个人消息集合

    Long findCountIsRead(@Param("user_id")String userId); //根据会员编号，查询已读-未读系统公告总数
    List<NoticeEdit> findListIsRead(@Param("user_id")String userId, Integer offset, Integer pageSize); //根据会员编号，查询已读-未读系统公告集合

    Long findCountNotRead(@Param("user_id")String userId); //根据会员编号，查询未读系统公告总数
    List<NoticeEdit> findListNotRead(@Param("user_id")String userId, Integer offset, Integer pageSize); //根据会员编号，查询未读系统公告集合

    Long findUserNotReadCount(@Param("user_id")String userId); //根据会员编号，查询个人公告未读总数
    List<NoticeEdit> findNotReadNoticeOneData(@Param("user_id")String userId); //查询 个人公告,系统公告未读 返回一条记录

    List<NoticeEdit> findReadNoticeOneData(@Param("user_id")String userId); //查询 个人公告,系统公告最近已读的数据 返回一条记录
}