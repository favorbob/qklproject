package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.admin.rebate.RebateProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SiteSettingServiceTest extends AbstractTestCase {

    @Autowired
    private SiteSettingService siteSettingService;

    @Test
    public void saveInvolSetting() {
        RebateProto.InvolSetting involSetting = new RebateProto.InvolSetting();
        List<RebateProto.RebateConfig> rebateConfigList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            RebateProto.RebateConfig config = new RebateProto.RebateConfig();
            config.setRatio(i + 1F);
            config.setRebateRatio(i + 1F);
            rebateConfigList.add(config);
        }
        involSetting.setRebateConfigList(rebateConfigList);
        involSetting.setAsset(BigDecimal.ONE);
        involSetting.setIntegral(new BigDecimal(3));
        involSetting.setAvailableAssets(new BigDecimal(70));
        involSetting.setLimitedAssets(new BigDecimal(30));
        involSetting.setInitIntegralRebateRatio(new BigDecimal(2));
        involSetting.setLinkedOnOff(true);
        involSetting.setSystemOnline(true);
        involSetting.setSystemOnLineTime(new Date());
        siteSettingService.saveInvolSetting(involSetting);
    }

    @Test
    public void getInvolSetting(){
        RebateProto.InvolSetting involSetting = siteSettingService.getInvolSetting();
        System.out.println("SiteSettingServiceTest.getInvolSetting:" + involSetting);
    }
}