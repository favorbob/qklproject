package cc.stbl.token.innerdisc.common.sms;

import cc.stbl.token.innerdisc.common.sms.client.ISmsClient;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.cogent.cache.CacheService;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsVerifyService {

    private static final Logger logger = LoggerFactory.getLogger(SmsVerifyService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ISmsClient smsClient;

    @Autowired
    private SmsProperties smsProperties;

    private static final String CACHE_MODULE = "vc";

    /**
     * @param businessKey
     * @param mobile
     * @param smsTemplateId
     * @param params 含短信码时，直接使用，不含时，自动生成一个。
     * @throws Exception
     */
    public void sendVerifySmsCode(String businessKey,String mobile, String smsTemplateId, Map<String, Object> params){
        if (params == null) {
            params = new HashMap<>();
        }
        String baseKey = this.getBaseKey(businessKey, mobile);
        String smscode = (String) params.get(smsProperties.getSmsCodeKey());
        if (smscode == null) {
            smscode = cacheService.get(baseKey, String.class);
        }
        if (smscode == null) {
            smscode = RandomStringUtils.randomNumeric(6);
        }
        if (smsProperties.isMock()) {
            smscode = "000000";
        }
        logger.info("下发短信码：{}-》{}", baseKey, smscode);
        params.put(smsProperties.getSmsCodeKey(), smscode);
        cacheService.put(baseKey, smscode, 5 * 60);
        smsClient.sendSms(mobile, smsTemplateId, params);

    }

    private String getBaseKey(String business, String key) {
        return CACHE_MODULE + ":" + business + ":" + key;
    }

    public void verifySmsCode(String businessKey, String mobile, String smsCode) {
        String baseKey = this.getBaseKey(businessKey, mobile);
        String redisSmsCode = cacheService.get(baseKey, String.class);
        logger.info("验证短信码：{}-》{} == {}", baseKey, redisSmsCode, smsCode);
        if (redisSmsCode == null || !redisSmsCode.equals(smsCode))
            throw new StructWithCodeException(ResponseCode.VERIFY_ERROR);
        cacheService.remove(baseKey);
    }
}
