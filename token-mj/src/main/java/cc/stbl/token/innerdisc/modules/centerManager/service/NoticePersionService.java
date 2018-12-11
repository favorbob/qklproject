package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.NoticeEditUserMapper;
import cc.stbl.token.innerdisc.modules.centerManager.dao.NoticePersonMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticeEditUser;
import cc.stbl.token.innerdisc.modules.centerManager.entity.NoticePerson;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticePersionService extends DataStoreServiceImpl<String, NoticePerson, NoticePersonMapper> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加或更新系统消息-个人记录
     * @param object 系统个人消息记录对象
     * @return 操作结果
     */
    public String addOrUpdateNoticePerson(NoticePerson object) {
        NoticePerson pexist = new NoticePerson();
        String noticeId = object.getNoticeId().trim();
        String userId = object.getUserId().trim();
        String msg = "";
        if(StringUtils.isNotEmpty(noticeId) && StringUtils.isNotEmpty(userId)) {
            //查询数据库是否已经存在记录
            pexist = mapper.selectByNoticeIdUserId(object);
            if ( pexist != null && StringUtils.isNotEmpty(pexist.getId()) ) {
                pexist.setCreateDate(new Date());
                int upnum = mapper.updateByPrimaryKey(pexist);  //更新创建时间
                logger.info("更新系统公告个人记录成功，更新记录" + upnum + "条，更新的公告id为：" + pexist.getNoticeId());
                msg = "更新系统公告个人记录成功，更新记录" + upnum + "条，更新的公告id为：" + pexist.getNoticeId();
            } else {
                object.setId(JavaUUIDGenerator.get());
                object.setCreateDate(new Date());
                object.setIsRead(2);    //已读
                int upnum  = mapper.insertSelective(object);
                logger.info("添加系统公告个人记录成功，更新记录" + upnum + "条，添加的公告id为：" + object.getNoticeId());
                msg = "添加系统公告个人记录成功，更新记录" + upnum + "条，添加的公告id为：" + object.getNoticeId();
            }
        } else {
            msg = "添加或更新系统公告失败，公告id或用户id为空";
        }
        return msg;
    }
}
