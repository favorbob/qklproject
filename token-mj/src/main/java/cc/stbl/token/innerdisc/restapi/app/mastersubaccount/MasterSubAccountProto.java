package cc.stbl.token.innerdisc.restapi.app.mastersubaccount;

import lombok.Data;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class MasterSubAccountProto {
    @Data
    public static class RequestDelete {
        @NotBlank(message = "请上传id")
        String id;
    }
    
    @Data
    public static class RequestAddMasterSubAccount {
        @NotBlank(message = "请输入账号")
        String phoneNum;
        @NotBlank(message = "请输入密码")
        String password;
    }
    
    @Data
    public static class RequestCardPage {
    	Integer pageNo = 1;
        Integer pageSize = 20;
    }
    
    
    
}
