/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import com.ks.common.datastore.model.Pager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.ActivateCardFlowMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivateCardFlowService extends DataStoreServiceImpl<String, ActivateCardFlow, ActivateCardFlowMapper> {

    //查询异动情况-分页
    public Pager<ActivateCardFlow> getActivateCardPage(ActivateCardFlow query, Integer pageNo, Integer pageSize) {

        Long total = mapper.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<ActivateCardFlow>(), total);
        }
        List<ActivateCardFlow> list = mapper.findList(query,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }
}