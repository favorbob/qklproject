package cc.stbl.token.innerdisc.common.sms.client;

import java.util.Map;

public interface ISmsClient {
    /**
     * @param mobile
     * @param templateId
     * @param params
     * @return 短信业务id
     */
    String sendSms(String mobile, String templateId, Map<String, Object> params);
}
