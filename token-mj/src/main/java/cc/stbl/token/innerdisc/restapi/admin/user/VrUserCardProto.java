package cc.stbl.token.innerdisc.restapi.admin.user;

import org.hibernate.validator.constraints.NotBlank;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;

import java.util.Date;

import javax.validation.constraints.NotNull;
import lombok.Data;

@ApiModel
public class VrUserCardProto {

  
    @Data
    public static class CreateUserCardRequest {
        @NotBlank(message = "请输入正确的手机号码")
        private String phoneNum;
        @NotNull(message = "请输入密码")
        private Integer num;
    }
    
    @Data
    public static class ListVrUserCardRequest  {
        
        private String phoneNum;
        private String cardType;
        private Integer status;
        private Date beginTime;
        private Date endTime;
        @NotNull(message = "pageNo不能为空")
        protected Integer pageNo;
        @NotNull(message = "pageSize不能为空")
        protected Integer pageSize;
    }
    @Data
    public static class ResponseUserCard{
	    private String id;
		private String cardNo;
		private String userId;
		private String phoneNum;
		private String cardType;
		private Date updateTime;
		private Integer status;
    }


}
