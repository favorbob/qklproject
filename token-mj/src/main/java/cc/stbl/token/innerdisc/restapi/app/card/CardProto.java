package cc.stbl.token.innerdisc.restapi.app.card;

import lombok.Data;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class CardProto {
    @Data
    public static class RequestGiveEachOther {
        @NotBlank(message = "请输入手机号")
        String phoneNum;
        @NotNull(message = "激活卡数量")
        Integer cardNum;
        @NotBlank(message = "请输入支付密码")
        String payPassword;
    }
    
    @Data
    public static class RequestCardNum {
        @NotBlank(message = "请输入userId")
        String userId;
    }
    
    @Data
    public static class RequestCardPage {
    	Integer pageNo = 1;
        Integer pageSize = 20;
    }
    
    
    
}
