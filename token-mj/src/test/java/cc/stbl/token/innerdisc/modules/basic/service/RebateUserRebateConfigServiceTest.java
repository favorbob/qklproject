package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RebateUserRebateConfigServiceTest extends AbstractTestCase {

    @Autowired
    private RebateUserRebateConfigService rebateConfigService;

    @Test
    public void testC() throws Exception {
        rebateConfigService.calculateUserRebateConfig();
    }
}
