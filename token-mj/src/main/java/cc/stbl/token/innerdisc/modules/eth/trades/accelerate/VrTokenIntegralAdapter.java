package cc.stbl.token.innerdisc.modules.eth.trades.accelerate;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.stbl.token.innerdisc.modules.basic.common.AreaUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserRebateConfig;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.RebateUserRebateConfigService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;

@Component
public class VrTokenIntegralAdapter extends AbstractIntegralAdapter{

    @Autowired
    private RebateUserRebateConfigService userRebateConfigService;

    @Autowired
    private VrUserInfoService vrUserInfoService;
    
    @Autowired
    private AreaUtil areaUtil;
    @Override
    public BigDecimal readRebateRatio(String userId) {
        BigDecimal base = super.readRebateRatio(userId); //0.02
        RebateUserRebateConfig rebateConfig = userRebateConfigService.get(userId);
        if(rebateConfig != null && rebateConfig.getCurrRebateRatio() > base.floatValue()) return new BigDecimal(rebateConfig.getCurrRebateRatio().toString());
        return base;
    }
    
    @Override
    //复投放大倍数
    public BigInteger readMagnification(String userId) {
    	VrUserInfo user = vrUserInfoService.get(userId);
    	Long aArea = user.getaArea();
    	Long bArea = user.getbArea();
    	int multiple = areaUtil.getAreaMultiple(aArea, bArea);
        return new BigInteger(String.valueOf(multiple));
    }
}
