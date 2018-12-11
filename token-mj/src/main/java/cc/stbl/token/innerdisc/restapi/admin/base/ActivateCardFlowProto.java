package cc.stbl.token.innerdisc.restapi.admin.base;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class ActivateCardFlowProto {

    @Data
    public static class RequestQuery extends BaseProto.RquestPager {
        //@NotBlank(message = "请输入会员编号")
        @ApiModelProperty("用户编号")
        private String phoneNum;
        @ApiModelProperty("开始时间")
        private Date startDate;
        @ApiModelProperty("结束时间")
        private Date endDate;
        @ApiModelProperty("改变原因")
        @NotBlank(message = "改变原因")
        private String changeReason;
    }
}
