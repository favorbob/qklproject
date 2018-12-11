package cc.stbl.token.innerdisc.restapi.app.user;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

public class UserRegisterProto {
    @Data
    public static class RequestRegister {
        @NotBlank(message = "请输入手机号")
        String phoneNum;
        @NotBlank(message = "请输入昵称")
        String userName;
        @NotBlank(message = "请输入正确的验证码")
        String smsCode;
        @NotBlank(message = "请输入密码")
        String password;
        @NotBlank(message = "请输入支付密码")
        String payPassword;
    }
}
