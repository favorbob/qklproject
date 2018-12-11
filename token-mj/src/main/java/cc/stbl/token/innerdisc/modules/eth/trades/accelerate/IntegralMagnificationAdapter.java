package cc.stbl.token.innerdisc.modules.eth.trades.accelerate;

import java.math.BigInteger;

/**
 * 转到积分放大的倍数
 */
public  interface IntegralMagnificationAdapter {

    /**
     * 获取倍数
     * @param userId
     * @return
     */
    BigInteger readMagnification(String userId);
}
