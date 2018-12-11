/**
* generator by mybatis plugin Wed Jul 18 09:07:54 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.entity.VrRedPaperLog;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VrUserTradesService{

    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private VrRedPaperLogService vrRedPaperLogService;


    /**
     * 领红包：
     * 用户每天仅可领取一次   通过红包录取记录判断是否多领
     * @param userId
     */
    public BigDecimal bonus(String userId) throws Exception {
        if(isBonus(userId)) throw new Exception("今日已领取过红包");

        BigDecimal bigDecimal = vrTokenTradeService.integralRebateByAmplify(userId);
        VrRedPaperLog vrRedPaperLog = new VrRedPaperLog(userId);
        vrRedPaperLog.setAmount(bigDecimal);
        vrRedPaperLogService.add(vrRedPaperLog);
        return bigDecimal;
    }

    public boolean isBonus(String userId){
        return vrRedPaperLogService.isReceive(userId);
    }
}