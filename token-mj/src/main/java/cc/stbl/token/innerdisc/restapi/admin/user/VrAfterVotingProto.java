package cc.stbl.token.innerdisc.restapi.admin.user;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Data;

@ApiModel
public class VrAfterVotingProto {

  

    @Data
    public static class ListVrAfterVotingRequest  {
        
        private String phoneNum;
        private String startDate;
        private String endDate;
        @NotNull(message = "pageNo不能为空")
        protected Integer pageNo;
        @NotNull(message = "pageSize不能为空")
        protected Integer pageSize;
    }

}
