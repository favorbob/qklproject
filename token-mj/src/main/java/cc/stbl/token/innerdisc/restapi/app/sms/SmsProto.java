package cc.stbl.token.innerdisc.restapi.app.sms;

import cc.stbl.token.innerdisc.restapi.app.login.AppLoginController;
import cc.stbl.token.innerdisc.restapi.app.user.UserRestController;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class SmsProto {

    @Data
    public static class RequestGetVerifyCode{
        @NotNull(message = "codeType不能为空")
        @ApiModelProperty(value = "业务类型: 0 注册, 1 忘记密码， 2 更换手机号，3 设置支付密码")
        private Integer codeType;// 0 注册   ， 1 忘记密码，2 更换手机号，3设置支付密码
        @NotBlank(message = "mobile不能为空")
        private String mobile; // 手机号

        public String getBusinessKey(){
            switch (codeType){
                case 1:
                    return UserRestController.class.getName();
                case 0:
                    return AppLoginController.class.getName();
                default:
                    return "";
            }
        }
    }
    @Data
    public static class RequestSendSmsNotice{
        @NotNull(message = "smsTemplateId不能为空")
        @ApiModelProperty(value = "短信模板ID")
        private String smsTemplateId;
        @NotBlank(message = "mobile不能为空")
        @ApiModelProperty(value = "短信接收手机号码")
        private String mobile; // 手机号
        @ApiModelProperty(value = "短信短信模板参数")
        private HashMap<String,Object> params;
    }
}
