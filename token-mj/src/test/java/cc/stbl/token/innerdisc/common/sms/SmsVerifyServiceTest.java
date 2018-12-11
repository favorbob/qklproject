package cc.stbl.token.innerdisc.common.sms;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class SmsVerifyServiceTest extends AbstractTestCase{

    @Autowired
    private SmsVerifyService smsVerifyService;

    @Test
    public void testSend() throws Exception {
        smsVerifyService.sendVerifySmsCode(SmsVerifyServiceTest.class.getName(),"18934790727","SMS_141760072",new HashMap<>());
    }

    @Test
    public void testVerifySmsCode() throws Exception {
        smsVerifyService.verifySmsCode(SmsVerifyServiceTest.class.getName(),"18934790727","703987");
    }
}
