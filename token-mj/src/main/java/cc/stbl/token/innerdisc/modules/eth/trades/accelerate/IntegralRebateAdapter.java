package cc.stbl.token.innerdisc.modules.eth.trades.accelerate;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 积分返利配置
 */
public  interface IntegralRebateAdapter {
    // 获得释放加速比
    BigDecimal readRebateRatio(String userId);

    //获得 可用资产 受限资产 比例  1 - 10
    BigInteger limitedRatio(String userId);

}
