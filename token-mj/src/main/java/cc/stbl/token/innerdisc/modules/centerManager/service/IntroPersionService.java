package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.centerManager.dao.IntroPersonMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroPerson;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IntroPersionService extends DataStoreServiceImpl<String, IntroPerson, IntroPersonMapper> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加或更新简介-个人记录
     * @param object 简介个人记录对象
     * @return 操作结果
     */
    public String addOrUpdateIntroPerson(IntroPerson object) {
        IntroPerson iexist = new IntroPerson();
        String introId = object.getIntroId().trim();
        String userId = object.getUserId().trim();
        String msg = "";
        if(StringUtils.isNotEmpty(introId) && StringUtils.isNotEmpty(userId)) {
            //查询数据库是否已经存在记录
            iexist = mapper.selectByIntroIdUserId(object);
            if ( iexist != null && StringUtils.isNotEmpty(iexist.getId()) ) {
                iexist.setCreateDate(new Date());
                int upnum = mapper.updateByPrimaryKey(iexist);  //更新创建时间
                logger.info("更新简介个人记录成功，更新记录" + upnum + "条，更新的公告id为：" + iexist.getIntroId());
                msg = "更新简介个人记录成功，更新记录" + upnum + "条，更新的公告id为：" + iexist.getIntroId();
            } else {
                object.setId(JavaUUIDGenerator.get());
                object.setCreateDate(new Date());
                object.setIsRead(2);    //已读
                int upnum  = mapper.insertSelective(object);
                logger.info("添加简介个人记录成功，更新记录" + upnum + "条，添加的公告id为：" + object.getIntroId());
                msg = "添加简介个人记录成功，更新记录" + upnum + "条，添加的公告id为：" + object.getIntroId();
            }
        } else {
            msg = "添加或更新简介失败，简介id或用户id为空";
        }

        return msg;
    }
}
