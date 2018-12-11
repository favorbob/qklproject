package cc.stbl.token.innerdisc.modules.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ks.common.datastore.exception.StructWithCodeException;

import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;
import cc.stbl.token.innerdisc.modules.eth.service.EthReturnedIntegralService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;

/**
 * @author leon
 * @Date 2018/10/15
 */
public class RetryReturnedIntegralScheduler implements SimpleJob{

    @Autowired
    private VrTokenTradeService tradeService;

    @Autowired
    private EthReturnedIntegralService returnedIntegralService;

    @Override
    public void execute(ShardingContext shardingContext) {
        EthReturnedIntegral query = new EthReturnedIntegral();
        query.setStatus(1);
        List<EthReturnedIntegral> returnedIntegrals = returnedIntegralService.findList(query);
        Long currentTime = System.currentTimeMillis();
        for (EthReturnedIntegral integral : returnedIntegrals) {
            Long ts = currentTime - integral.getOptDate().getTime();
            if(ts >  1000 * 60 * 60){ //2小时
                try {
                    Thread.sleep(200);
                    tradeService.integralReturn(integral.getUserId(),integral.getBalance(),integral.getLimitedBalance(),integral.getIntegral(),integral.getId() + "_" + ts);
                    integral.setStatus(3);
                    returnedIntegralService.update(integral);
                } catch (StructWithCodeException codeException) {
                    integral.setStatus(3);
                    returnedIntegralService.update(integral);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
