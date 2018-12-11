package cc.stbl.token.innerdisc.restapi.app.aftervoting;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cc.stbl.token.innerdisc.restapi.app.user.VrUserTradeProto;
import lombok.Data;

public class VrAfterVotingProto {

	
    
    @Data
    public static class AfterVotingRequest  {
        
    	@NotNull(message = "请输入数量")
        private int num;
    	@NotBlank(message = "支付密码")
        private String payPassword;
      
    }
    
    @Data
    public static class ListVRequest  {

    	private int pageNo = 1;
    	private int pageSize = 20;
    }
}
