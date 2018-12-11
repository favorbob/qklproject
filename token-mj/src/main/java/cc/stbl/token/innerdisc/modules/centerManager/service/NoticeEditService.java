package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.NoticeEditMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEdit;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticeEditService extends DataStoreServiceImpl<String, NoticeEdit, NoticeEditMapper> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NoticeEditUserService noticeEditUserService;

    /**
     * 添加公告
     * @param noticeEdit
     * @return
     */
    public String insertNoticeEdit(NoticeEdit noticeEdit) {
        String consigneeNum = noticeEdit.getConsigneeNum().trim();
        String msg = "";
        if( !(noticeEdit.getMsgType() == 1) && !(noticeEdit.getMsgType() == 2) ) {
            msg = "添加公告失败，消息类型值不正确，必须为1或者2！";
            return  msg;
        }
        if( !(noticeEdit.getIsPop() == 1) && !(noticeEdit.getIsPop() == 2) ) {
            msg = "添加公告失败，是否弹窗，必须为1或者2！";
            return  msg;
        }
        //个人消息
        if( noticeEdit.getMsgType() == 2 ) {
            if (StringUtils.isEmpty(consigneeNum)) {
                msg = "添加公告失败，收件人编号不能为空！";
                return msg;
            }
        }
        /*if( noticeEdit.getMsgType() == 2 ) {    //个人消息
            //收件人编码存在的时候
            if( StringUtils.isNotEmpty(consigneeNum) ) {
                //收件人编号在 收件人表中要存在
                int num = noticeEditUserService.findCountByConsignee(consigneeNum);
                if(num == 0) {
                    msg = "添加公告失败，不存在收件人编码对应的收件人";
                    return  msg;
                }
            }
        }*/

        noticeEdit.setId(JavaUUIDGenerator.get());  //id随机生成
        noticeEdit.setCreateDate(new Date());
        noticeEdit.setStatue(1);    //添加，默认新增状态

        int num = this.mapper.insertSelective(noticeEdit);
        return  "添加公告成功，添加了" + num + "条记录！";
    }


    /**
     * 更新公告
     * @param noticeEdit
     * @return
     */
    public String updateNoticeEdit(NoticeEdit noticeEdit) {
        String consigneeNum =  noticeEdit.getConsigneeNum().trim();
        String msg = "";
        if( !(noticeEdit.getMsgType() == 1) && !(noticeEdit.getMsgType() == 2) ) {
            msg = "更新公告失败，消息类型值不正确，必须为1或者2！";
            return  msg;
        }
        if( !(noticeEdit.getIsPop() == 1) && !(noticeEdit.getIsPop() == 2) ) {
            msg = "更新公告失败，是否弹窗，必须为1或者2！";
            return  msg;
        }
        //个人消息
        if( noticeEdit.getMsgType() == 2 ) {
            if (StringUtils.isEmpty(consigneeNum)) {
                msg = "添加公告失败，收件人编号不能为空！";
                return msg;
            }
        }
        noticeEdit.setUpdateDate(new Date());
        noticeEdit.setStatue(2);    //更新为修改状态
        int num = this.mapper.updateByPrimaryKeySelective(noticeEdit);  //更新的包含简历公告
        return "更新公告成功，更新了" + num + "条记录！";
    }

    /**
     * 发布公告-更新公告状态
     * @param noticeEdit
     * @return
     */
    public String deployNotice(NoticeEdit noticeEdit) {
        noticeEdit.setStatue(9); //状态 9-生效
        noticeEdit.setUpdateDate(new Date());
        int num = this.mapper.updateByPrimaryKeySelective(noticeEdit);  //更新的包含简历公告
        return "发布公告成功！";
    }

    /**
     * 查询公告分页信息，不包含公告内容
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<NoticeEdit> findPageNotContext(NoticeEdit query, Integer pageNo, Integer pageSize) {
        Long total = this.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<NoticeEdit>(pageNo,pageSize,new ArrayList<NoticeEdit>(),total);
        }
        List<NoticeEdit> list = this.findList(query,offset,pageSize);
        for(NoticeEdit notic : list) {
            notic.setNoticeContext(""); //设置公告内容为空
        }
        return new Pager<NoticeEdit>(pageNo,pageSize,list,total);
    }

    /**
     * 查询个人公告信息-分页
     * @param userId 个人的UserId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<NoticeEdit> findUserNoticePage(String userId, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findUserNoticeCount(userId);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<NoticeEdit>(pageNo,pageSize,new ArrayList<NoticeEdit>(),total);
        }
        List<NoticeEdit> list = this.mapper.findUserNoticeList(userId,offset,pageSize);
        return new Pager<NoticeEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-系统公告信息-分页。包含已读、未读的系统公告
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<NoticeEdit> findSysNoticeAllReadPage(String userId, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountIsRead(userId);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<NoticeEdit>(pageNo,pageSize,new ArrayList<NoticeEdit>(),total);
        }
        List<NoticeEdit> list = this.mapper.findListIsRead(userId,offset,pageSize); //根据会员编号，查询已读-未读系统公告集合
        return new Pager<NoticeEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-系统公告信息-分页。包含未读的系统公告
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<NoticeEdit> findSysNoticeNotReadPage(String userId, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountNotRead(userId);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<NoticeEdit>(pageNo,pageSize,new ArrayList<NoticeEdit>(),total);
        }
        List<NoticeEdit> list = this.mapper.findListNotRead(userId,offset,pageSize); //根据会员编号，查询未读系统公告集合
        return new Pager<NoticeEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-获取个人系统消息 未读总数
     * @param userId
     * @return
     */
    public int getSysPersonNotRead(String userId) {
        Long total = this.mapper.findCountNotRead(userId);
        return total.intValue();
    }

    /**
     * app查询-获取个人公告 未读总数
     * @param userId
     * @return
     */
    public int findUserNotReadCount(String userId) {
        Long total = this.mapper.findUserNotReadCount(userId);
        return total.intValue();
    }

    /**
     * app查询-查询系统公告、个人公告 未读1条记录。有多条未读，只是返回一次未读。
     * @param userId
     * @return
     */
    public List<NoticeEdit> getNotReadNoticeOne(String userId) {
        List<NoticeEdit> listreturn = new ArrayList<NoticeEdit>();
        List<NoticeEdit> listno = new ArrayList<NoticeEdit>();
        List<NoticeEdit> listRead = new ArrayList<NoticeEdit>();
        listno = this.mapper.findNotReadNoticeOneData(userId); //根据会员编号，查询未读系统公告的一条记录
        listRead = this.mapper.findReadNoticeOneData(userId);  //根据会员编号，查询最近已读系统公告的一条记录
        if( listno.size() > 0 ) {
            if( listRead.size() > 0 ) {
                logger.info("系统消息未读数:" +listno.size() + ":" + listno.get(0).getId() +":" + listno.get(0).getUpdateDate());
                logger.info("系统消息已读数:" +listRead.size() + ":" + listRead.get(0).getId() +":" + listRead.get(0).getUpdateDate());
                NoticeEdit noread = listno.get(0);
                NoticeEdit isread = listRead.get(0);
                //未读公告的时间 大于 已读公告的时间
                if( noread.getUpdateDate().getTime() > isread.getUpdateDate().getTime() ) {
                    listreturn = listno  ;  //返回未读的一条记录
                } else {
                    // 返回空，不给前端弹窗提示
                }
            } else {
                //无已读记录
                listreturn = listno; //直接返回一条未读数据
            }
        }
        return listreturn;
    }

}
