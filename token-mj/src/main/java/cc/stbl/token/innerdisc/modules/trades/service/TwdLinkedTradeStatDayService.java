/**
* generator by mybatis plugin Wed Aug 22 17:48:05 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.trades.service;

import cc.stbl.token.innerdisc.modules.trades.dao.TwdLinkedTradeStatDayMapper;
import cc.stbl.token.innerdisc.modules.trades.entity.TwdLinkedTradeStatDay;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwdLinkedTradeStatDayService extends DataStoreServiceImpl<Date, TwdLinkedTradeStatDay, TwdLinkedTradeStatDayMapper> {

    @Autowired
    private TwdLinkedTradeRecordService twdLinkedTradeRecordService;

    public void addTwdLinkedTradeStatDay(){
        TwdLinkedTradeStatDay twdLinkedTradeStatDay = twdLinkedTradeRecordService.countTwdLinkedTradeStatDay();
        super.add(twdLinkedTradeStatDay);
    }

    public TwdLinkedTradeStatDay getStatisticalTradeAsset(){
        return super.get(new Date());
    }
}