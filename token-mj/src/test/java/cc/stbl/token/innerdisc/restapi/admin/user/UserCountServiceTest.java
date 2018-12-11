package cc.stbl.token.innerdisc.restapi.admin.user;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCountService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCountServiceTest extends AbstractTestCase {

    @Autowired
    private VrUserCountService vrUserCountService;

    @Test
    public void testInsertUserCount(){
        vrUserCountService.selectInsertUserCount();
    }

}
