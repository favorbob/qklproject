package cc.stbl.token.innerdisc.modules.centerManager.service;

import cc.stbl.token.innerdisc.modules.centerManager.dao.IntroEditMapper;
import cc.stbl.token.innerdisc.modules.centerManager.entity.IntroEdit;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntroEditService  extends DataStoreServiceImpl<String, IntroEdit, IntroEditMapper> {

    /**
     * 查询简介分页信息，不包含简介内容
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<IntroEdit> findPageNotContext(IntroEdit query, Integer pageNo, Integer pageSize) {
        Long total = this.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<IntroEdit>(pageNo,pageSize,new ArrayList<IntroEdit>(),total);
        }
        List<IntroEdit> list = this.findList(query,offset,pageSize);
        for(IntroEdit intro : list) {
            intro.setIntroContext("");  //设置简介内容为空
        }
        return new Pager<IntroEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-简介信息-分页。包含已读、未读的简介
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<IntroEdit> findIntroAllReadPage(String userId, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountIsRead(userId);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<IntroEdit>(pageNo,pageSize,new ArrayList<IntroEdit>(),total);
        }
        List<IntroEdit> list = this.mapper.findListIsRead(userId,offset,pageSize); //根据会员编号，查询已读-未读简介集合
        return new Pager<IntroEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-简介信息-分页。包含未读的简介
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<IntroEdit> findIntroNotReadPage(String userId, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountNotRead(userId);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<IntroEdit>(pageNo,pageSize,new ArrayList<IntroEdit>(),total);
        }
        List<IntroEdit> list = this.mapper.findListNotRead(userId,offset,pageSize); //根据会员编号，查询未读简介集合
        return new Pager<IntroEdit>(pageNo,pageSize,list,total);
    }

    /**
     * app查询-获取个人简介消息 未读总数
     * @param userId
     * @return
     */
    public int getIntroPersonNotRead(String userId) {
        Long total = this.mapper.findCountNotRead(userId);
        return total.intValue();
    }
}
