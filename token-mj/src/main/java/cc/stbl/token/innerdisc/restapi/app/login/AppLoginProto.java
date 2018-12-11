package cc.stbl.token.innerdisc.restapi.app.login;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

public class AppLoginProto {

    @Data
    public static class RequestLogin{
        @NotBlank(message = "请输入手机号")
        private String mobile;
        @NotBlank(message = "请输入密码")
        private String password;
        private Boolean rememberMe = false;
        @NotBlank(message = "请填写设备号")
        String deviceId;
    }

    @Data
    public static class ResponseLogin{
        private Boolean rememberMe = false;
        private String token;
        private String UID;
        private String userType;
        private String userName;
        private String phoneNum;
        private String userIcon;
        private String userId;
        private String ethReceiptCodeImg;
        private String hasWallet;
    }

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
          @NotBlank(message = "parentUserId为空")
          String parentUserId;
          @NotBlank(message = "请填写A区或者B区")
          String area;

    }
    
  
}
