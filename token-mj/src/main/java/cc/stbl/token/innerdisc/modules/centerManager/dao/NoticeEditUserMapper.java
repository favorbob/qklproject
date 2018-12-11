package cc.stbl.token.innerdisc.modules.centerManager.dao;

import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEditUser;
import com.ks.common.datastore.dao.IDataStoreMapper;

import java.util.List;

public interface NoticeEditUserMapper extends IDataStoreMapper<String, NoticeEditUser> {
    int deleteByPrimaryKey(String id);

    int insert(NoticeEditUser record);

    int insertSelective(NoticeEditUser record);

    NoticeEditUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NoticeEditUser record);

    int updateByPrimaryKey(NoticeEditUser record);

    Long deleteByNoticeConsignee(String consigneeNum);   //根据收件人编码删除数据
    Long findCountByConsignee(String consigneeNum);  //根据收件人编码查询统计
    List<NoticeEditUser> findListByConsignee(String consigneeNum); //根据收件人编码查询所有的收件人
    void updateStatueByIds(String[] ids);   //更新收件人为无效状态
    List<NoticeEditUser> findUseListByConsignee(String consigneeNum); //根据收件人编码查询有效的收件人
    int updateIsReadByUserIdAndNum(NoticeEditUser record);  //根据收件人编码，userId更新公告为已读
}