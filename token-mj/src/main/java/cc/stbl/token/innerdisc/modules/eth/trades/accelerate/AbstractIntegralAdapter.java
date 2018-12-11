package cc.stbl.token.innerdisc.modules.eth.trades.accelerate;

import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractIntegralAdapter implements IntegralMagnificationAdapter,IntegralRebateAdapter {

    @Autowired
    private SiteSettingService siteSettingService;

    @Override
    //复投放大倍数
    public BigInteger readMagnification(String userId) {
        return siteSettingService.getMagnification().toBigInteger();
    }

    @Override
    //领红包加上比，释放率
    public BigDecimal readRebateRatio(String userId) {
        BigDecimal rebateRatio = siteSettingService.getIntegralRebateRatio();
        rebateRatio = (rebateRatio == null) ? new BigDecimal(0.002) : rebateRatio.divide(new BigDecimal(100));
        return rebateRatio;
    }

    @Override
    //  aiic 和 mj 比例 ,  1 - 10 , 1 表示 aiic 10%, mj =90% ,以此类推
    public BigInteger limitedRatio(String userId) {
        return  siteSettingService.getLimitedRatio().toBigInteger();
    }
}
