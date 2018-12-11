package cc.stbl.token.innerdisc.common.push;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.push.inter.model.PushPayload;
import cc.stbl.token.innerdisc.common.push.inter.model.PushResult;
import cc.stbl.token.innerdisc.common.push.service.PushService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PushTest extends AbstractTestCase {

    @Autowired
    private PushService pushService;

    @Test
    public void testPush(){

        PushPayload pushPayload = PushPayloadUtil.buildPushObject_all_all_alert("test alert");
        PushResult result = pushService.push(pushPayload);
        System.out.print("推送测试结果：" + JSON.toJSONString(result));
    }

}
