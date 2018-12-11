package cc.stbl.token.innerdisc.restapi.app.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import cc.stbl.token.innerdisc.restapi.app.login.AppLoginProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
public class UserProto {
	
    @Data
    public static class RequestForgotPasswordFirstStep{
        @NotBlank(message = "请输入手机号")
        private String phoneNum;
        private String smsCode;
    }
    
    @Data
    public static class RequestForgotPasswordSecondStep{
        @NotBlank(message = "请输入密码")
        private String password1;
        @NotBlank(message = "请输入密码")
        private String password2;
        @NotBlank(message = "请填写电话号码")
        private String phoneNum;
        
    }

    @Data
    public static class RequestThreeUsers{
        @NotBlank(message = "请输入手机号")
        private String phoneNum;
    }
    

    @Data
    public static class RequestRestPassword{
        @NotBlank(message = "请输入原密码")
        private String oldPassword;
        @NotBlank(message = "请输入新密码")
        private String newPassword;
    }
    
    
    @Data
    public static class RequestUserDelete{
        @NotBlank(message = "请输入userid")
        private String userId;
    }
    
    
    @Data
    public static class RequestActiveUser{
        @NotBlank(message = "请输入手机号码")
        private String phoneNum;
        @NotNull(message = "请选择激活类型")
        private Integer activeMoney;//300或者1500
    }
    
    

    @Data
    public static class RequestModifyUserNickName{
        @NotBlank(message = "请输入昵称")
        private String nickName;
    }

    @Data
    public static class RequestRestPayPassword{
        @NotBlank(message = "请输入新密码")
        private String newPassword;
        @NotBlank(message = "请输入正确的验证码")
        String smsCode;
    }
    
    
    @Data
    public static class RequestTransferMJOrAIIC{
        @NotBlank(message = "请输入手机号码")
        private String phoneNum;
        @NotNull(message = "请输入数量")
        private String num;
        @NotBlank(message = "请输入支付密码")
        private String payPassword;
    }


    @Data
    public static class RequestModifyMobileCheck{
        @NotBlank(message = "请输入手机号")
        private String mobile;
        @NotBlank(message = "请输入密码")
        private String password;
    }

    @Data
    public static class RequestModifyMobile{
        @NotBlank(message = "请输入手机号")
        private String mobile;
        @NotBlank(message = "请输入验证码")
        private String smsCode;
        @NotBlank(message = "请输入新密码")
        private String newPassword;
        @NotNull
        @ApiModelProperty(value = "业务类型: 0 注册, 1 忘记密码， 2 更换手机号，3 设置支付密码")
        private Integer codeType;
    }

    @Data
    public static class RequestRetrievePassword{
        @NotBlank(message = "请输入手机号")
        private String mobile;
        @NotBlank(message = "请输入验证码")
        private String smsCode;
        @NotBlank(message = "请输入新密码")
        private String newPassword;
        @NotNull
        @ApiModelProperty(value = "业务类型: 0 注册, 1 忘记密码， 2 更换手机号，3 设置支付密码")
        private Integer codeType;
    }

    @Data
    public static class RequestTransfer{

        @NotBlank(message = "请输入对方账号")
        @ApiModelProperty("对方账号")
        private String account;

        @DecimalMin("1")
        @ApiModelProperty("转出资产")
        private BigDecimal assets;

        @ApiModelProperty("支付密码")
        @NotBlank(message = "请输入支付密码")
        private String payPassword;

        @ApiModelProperty("备注")
        private String remark;
    }

    @Data
    public static class RequestMyPromotion extends BaseProto.RquestPager {
        private Date date;
    }

    @Data
    public static class ResponseMyPromotion extends BaseProto.RquestPager {
        private String userId;
        @ApiModelProperty("邀请码")
        private String inviteCode;
        @ApiModelProperty("邀请码图片")
        private String inviteCodeImg;
        @ApiModelProperty("我的会员列表")
        private List<ResponseSubInfo> lowerInfoList;

    }

    @Data
    public static class ResponseSubInfo {
        private String userId;
        private String nickName;
        private String mobile;
        @ApiModelProperty("贡献的资产")
        private BigDecimal contributedAssets;
    }

    @Data
    public static class RequestSaveImg{
        @ApiModelProperty("相对路径")
        private String path;
        @ApiModelProperty("类型：1微信码/2支付宝码")
        private Integer type;
    }

    @Data
    public static class ReceiptCode{
        private String url;
        @ApiModelProperty("类型：1微信码/2支付宝码")
        private Integer type;
    }
}
