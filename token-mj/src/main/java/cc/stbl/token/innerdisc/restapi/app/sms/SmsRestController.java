package cc.stbl.token.innerdisc.restapi.app.sms;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
import cc.stbl.token.innerdisc.common.sms.client.ISmsClient;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/sms"})
@Api(description = "短信服务")
public class SmsRestController {

    @Autowired
    private SmsVerifyService smsVerifyService;
    @Autowired
    private ISmsClient smsClient;

    @RequestMapping(value = {"/getVerifyCode"},method = RequestMethod.POST)
    @ApiOperation("发送短信")
    public Response getVerifyCode(@RequestBody @Valid SmsProto.RequestGetVerifyCode request){
//        smsVerifyService.sendVerifySmsCode(request.getCodeType().toString(),request.getMobile(),"SMS_141760073",new HashMap<>());
        smsVerifyService.sendVerifySmsCode(request.getCodeType().toString(),request.getMobile(),"SMS_146805586",new HashMap<>());
    	return Response.success("123123");
    }
    @RequestMapping(value = {"/sendSmsNotice"},method = RequestMethod.POST)
    @ApiOperation("发送短信通知")
    public Response getVerifyCode(@RequestBody @Valid SmsProto.RequestSendSmsNotice request){
        smsClient.sendSms(request.getMobile(), request.getSmsTemplateId(), request.getParams());
        return Response.success();
    }

}
