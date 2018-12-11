package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class CenterEditServiceProto {

    @Data
    public static class ESRequestList {
        @ApiModelProperty("headLine")
        private String headLine;
        @ApiModelProperty("内容")
        private String context;
    }

    @Data
    public static class ResponseGetSysLogInfo {
        @ApiModelProperty("用户编号")
        private String userId;
        @ApiModelProperty("ip地址")
        private String ipAddress;
        @ApiModelProperty("用户类型 1系统用户/ 2团队领导/ 9会员")
        private Integer userType;
        @ApiModelProperty("创建时间")
        private Date createDate;
        @ApiModelProperty("开始时间")
        private Date endDate;
    }

    @Data
    public static class Id {
        @ApiModelProperty("客服标题id")
        @NotBlank(message = "请输入客服标题id")
        private String id;
    }
    @Data
    public static class ServiceId {
        @ApiModelProperty("客服图片id")
        @NotBlank(message = "请输入客服图片id")
        private String id;
    }
    
    @Data
    public static class addEntity {
        private String remarks;
        private String serviceName;
    }
    
    
    @Data
    public static class ServicePage {
          protected Integer pageNo;
          protected Integer pageSize;
    }
    
    //分页使用
    @Data
    public static class EditServiceId extends BaseProto.RquestPager {
        @ApiModelProperty("客服标题id")
        @NotBlank(message = "请输入客服标题id")
        private String editServiceId;
        private Integer pageNo;
        private Integer pageSize;
    }
}
