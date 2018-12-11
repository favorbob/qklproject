package cc.stbl.token.innerdisc.restapi.app.user;

import org.hibernate.validator.constraints.NotBlank;

import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;

import java.util.Date;

import javax.validation.constraints.NotNull;
import lombok.Data;

@ApiModel
public class VrUserTradeProto {

  

    @Data
    public static class ListVrUserTradeRequest  {
        
        private String inAccount;
        private String outAccount;
        private Integer tradeStatus;
        private Integer tradeType;
        private String beginTime;
        private String endTime;
        @NotNull(message = "pageNo不能为空")
        protected Integer pageNo;
        @NotNull(message = "pageSize不能为空")
        protected Integer pageSize;
    }
    
    
    @Data
    public static class AfterVotingRequest  {
        
        private String num;
        private String payPassword;
      
    }

}
