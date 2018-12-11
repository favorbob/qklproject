package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.NoticeEditUserMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEditUser;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticeEditUserService extends DataStoreServiceImpl<String, NoticeEditUser, NoticeEditUserMapper> {

    /**
     * 根据收件人编码删除收件人数据
     * @param noticeConsignee
     * @return
     */
    public Long deleteByNoticeConsignee(String noticeConsignee) {
        Long num = this.mapper.deleteByNoticeConsignee(noticeConsignee)  ;
        return  num;
    }

    /**
     * 根据收件人编码 查询对应收件人数量
     * @param noticeConsignee
     * @return
     */
    public Long findCountByConsignee(String noticeConsignee) {
        return this.mapper.findCountByConsignee(noticeConsignee);
    }

    /**
     * 根据收件人编码查询所有的收件人
     * @param noticeConsignee
     * @return
     */
    public List<NoticeEditUser> findListByConsignee(String noticeConsignee) {
        return this.mapper.findListByConsignee(noticeConsignee);
    }

    /**
     * 根据收件人编码查询有效的收件人
     * @param noticeConsignee
     * @return
     */
    public List<NoticeEditUser> findUseListByConsignee(String noticeConsignee) {
        return this.mapper.findUseListByConsignee(noticeConsignee);
    }


    /**
     * 批量添加/更新公告收件人
     * @param consigneeNum 收件人编号
     * @param userId  用户id，多个用，分割
     * @param userName 姓名，多个用，分割
     * @return
     */
    public String insertAndUpdateNoticeUser(String consigneeNum, String userId, String userName ) {
        String msg = "";
        if (StringUtils.isEmpty(consigneeNum)) {
            msg = "添加公告收件人，收件人编号不能为空！";
            return msg;
        }
        String[] userIds = userId.split(",");
        String[] userNames = userName.split(",");

        if(userIds.length != userNames.length) {
            msg = "添加公告收件人，用户名id个数和姓名个数不一致";
            return msg;
        }
        List<NoticeEditUser> editUsers = new ArrayList<>();

        //判断数据库是否已经存在记录
        Long fnum = findCountByConsignee(consigneeNum.trim());
        if( fnum > 0) { //修改
            //查询出原来记录
           List<NoticeEditUser> oldEitUser =  this.mapper.findListByConsignee(consigneeNum.trim());
           String[] ids = getUpdateStatueIds(userIds, oldEitUser);  //得到需要处理原ids集合
           if(ids.length > 0 ) {
                //更新收件人为失效状态
                this.mapper.updateStatueByIds(ids);
           }
           //得到修改+新增的集合
           editUsers = getExsitAndNew(userIds, userNames, oldEitUser);
        } else  {   //新增
            //封装，添加内容
            for(int i=0; i<userIds.length; i++) {
                NoticeEditUser editUser = new NoticeEditUser();
                editUser.setId(JavaUUIDGenerator.get());
                editUser.setNoticeConsignee(consigneeNum.trim());  //设置收件人编码
                editUser.setUserId(userIds[i].trim());     //userId
                editUser.setUserName(userNames[i].trim()); //姓名
                editUser.setStatue(1);      //有效
                editUser.setIsRead(1);      //未读
                editUser.setCreateDate(new Date());
                editUsers.add(editUser) ;
            }
        }
        this.mapper.batchInsertSelective(editUsers); //批量收件人
        return  "批量添加/更新收件人成功，增加" +userIds.length +"个人！";
    }

    /**
     *  根据用户id得到原收件人id相同收件人的收件人+ 新的userid的收件人 ，
     * @param userIds 用户id字符串数组
     * @param userNames 用户姓名字符串数组
     * @param oldEitUser 集合
     * @return
     */
    public List<NoticeEditUser> getExsitAndNew(String[] userIds, String[] userNames, List<NoticeEditUser> oldEitUser ) {
        List<NoticeEditUser> noticeEditUsers = new ArrayList<NoticeEditUser>();
        String consigneeNum = oldEitUser.get(0).getNoticeConsignee();   //获取一个收件人编码
        int flag = 0 ;
        for(int i=0; i<userIds.length; i++) {
            flag  = 0;
            for(NoticeEditUser euse : oldEitUser) {
                //userId相同，得到原数据库的记录，设置为消息未读（再次发送）
                if( userIds[i].trim().equals(euse.getUserId()) ) {
                    NoticeEditUser editUser = new NoticeEditUser();
                    editUser.setId(euse.getId());
                    editUser.setNoticeConsignee(euse.getNoticeConsignee());
                    editUser.setUserId(euse.getUserId());
                    editUser.setUserName(userNames[i].trim());
                    editUser.setStatue(1);  // 状态有效
                    editUser.setIsRead(1);  //未读
                    editUser.setCreateDate(euse.getCreateDate());
                    editUser.setUpdateDate(new Date()); //设置最后一次更新时间

                    noticeEditUsers.add(editUser);
                    flag = 1;
                    break;
                }
            }
            if(flag == 0) {
                NoticeEditUser editUser = new NoticeEditUser();
                editUser.setId(JavaUUIDGenerator.get());
                editUser.setNoticeConsignee(consigneeNum.trim());  //设置收件人编码
                editUser.setUserId(userIds[i].trim());     //userId
                editUser.setUserName(userNames[i].trim()); //姓名
                editUser.setStatue(1);      //有效
                editUser.setIsRead(1);      //未读
                editUser.setCreateDate(new Date());
                noticeEditUsers.add(editUser) ;
            }
        }
        return noticeEditUsers;
    }

    /**
     * 得到原数据库中存在的收件人Ids（去除与userIds 相同的），可能为得到的是空
     * @param userIds
     * @param oldEitUser
     * @return
     */
    public String[] getUpdateStatueIds(String[] userIds, List<NoticeEditUser> oldEitUser ) {
        List<NoticeEditUser> noticeEditUsers = new ArrayList<NoticeEditUser>();
        int flag = 0 ;
        StringBuffer ssbb = new StringBuffer();
        for(NoticeEditUser euse : oldEitUser) {
            flag = 0 ;
            for(int i=0; i<userIds.length; i++) {
                //如果userid相同
                if( euse.getUserId().trim().equals(userIds[i].trim()) ) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 0) {
                ssbb.append(euse.getId().trim()).append(",");
            }
        }
        return ssbb.toString().split(",");
    }

    /**
     * 更新个人公告为已读
     * @param noticeUser
     * @return
     */
    public String updatePersonNotice(NoticeEditUser noticeUser) {
        noticeUser.setIsRead(2); //状态 2-已读
        noticeUser.setUpdateDate(new Date());   //更新时间
        int num = this.mapper.updateIsReadByUserIdAndNum(noticeUser);  //更新的个人公告为已读
        return "更新个人公告为已读！";
    }

}
