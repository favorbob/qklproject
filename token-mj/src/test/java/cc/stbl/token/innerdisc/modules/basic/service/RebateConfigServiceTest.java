package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.admin.rebate.RebateProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RebateConfigServiceTest extends AbstractTestCase {

    @Autowired
    private RebateConfigService rebateConfigService;

    @Test
    public void addRebateConfig() {
        RebateProto.RebateConfig rebateConfig = new RebateProto.RebateConfig();
        rebateConfig.setRatio(1F);
        rebateConfig.setRebateRatio(0.02F);
        rebateConfigService.addRebateConfig(rebateConfig);
        List<cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig> all = rebateConfigService.findAll();
        for (cc.stbl.token.innerdisc.modules.basic.entity.RebateConfig vo : all){
            System.out.println("RebateConfigServiceTest.addRebateConfig" + vo);
        }
    }
}