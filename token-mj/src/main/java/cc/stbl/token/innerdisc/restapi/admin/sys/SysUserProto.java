package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class SysUserProto {


    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "请输入正确的手机号码")
        private String phoneNum;
        @NotBlank(message = "请输入密码")
        private String password;
        @ApiModelProperty("用户等级,1:系统用户，2:团队领导，9:普通用户")
        @NotNull(message = "请选择用户等级")
        private Integer userLevel;
    }
    @Data
    public static class UpdatePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Data
    public static class ListRequest extends BaseProto.RquestPager{}

    @Data
    public static class ResponseGetSysUserInfo {

        private String userId;

        @ApiModelProperty("用户名 登录名")
        private String loginName;
        @ApiModelProperty("用户姓名")
        private String name;
        @ApiModelProperty("手机号")
        private String phone;
        @ApiModelProperty("用户类型 1系统用户/ 2团队领导/ 9会员")
        private Integer userType;
        @ApiModelProperty("头像")
        private String photo;
        @ApiModelProperty("注册时间")
        private Date createDate;
    }



    @Data
    public static class NameListRequest extends BaseProto.RquestPager{
        @ApiModelProperty("姓名")
        private String name;
    }

    @Data
    public static class RequestSysUserInfo {
        @ApiModelProperty("userId")
        @NotBlank(message = "请输入正确的userId")
        private String userId;
        @ApiModelProperty("工号")
        private String no;
        @ApiModelProperty("用户姓名")
        private String name;
        @ApiModelProperty("邮箱")
        private String email;
        @ApiModelProperty("电话")
        private String phone;
        @ApiModelProperty("手机")
        private String mobile;
        @ApiModelProperty("用户类型")
        private Integer userType;
        @ApiModelProperty("是否可登陆")
        private Integer status;
    }

}
