/**
* generator by mybatis plugin Fri Aug 24 14:52:32 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.VrRedPaperLogMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrRedPaperLog;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VrRedPaperLogService extends DataStoreServiceImpl<String, VrRedPaperLog, VrRedPaperLogMapper> {

    /**
     * 是否领取过红包
     * @param userID
     * @return
     */
    public boolean isReceive(String userID){
        VrRedPaperLog vrRedPaperLog = new VrRedPaperLog(userID);
        vrRedPaperLog.setTime(new Date());
        Long count = super.findCount(vrRedPaperLog);
        if(count != null && count > 0) return true;
        return false;
    }
}