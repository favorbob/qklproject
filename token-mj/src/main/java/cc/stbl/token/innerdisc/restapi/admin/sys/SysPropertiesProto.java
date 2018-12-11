package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class SysPropertiesProto {

    @Data
    public static class AddPropsRequest {
        @NotBlank(message = "请输入参数名称")
        private String name;
        @ApiModelProperty("显示的label")
        private String label;
        @ApiModelProperty("参数值")
        private String value;
        @ApiModelProperty("类型")
        @NotBlank(message = "类型")
        private String kind;
    }

    @Data
    public static class PropsRequest {
        @NotBlank(message = "请输入id")
        private String id;
        @NotBlank(message = "请输入参数名称")
        private String name;
        @ApiModelProperty("显示的label")
        private String label;
        @ApiModelProperty("参数值")
        @NotBlank(message = "参数值")
        private String value;
        @ApiModelProperty("类型")
        @NotBlank(message = "类型")
        private String kind;
        @ApiModelProperty("类型")
        private Integer sort;
        @ApiModelProperty("创建时间")
        private Date createDate;
    }

    @Data
    public static class PropsRequestList extends BaseProto.RquestPager {
        @NotBlank(message = "请输入id")
        private String id;
        @NotBlank(message = "请输入参数名称")
        private String name;
        @ApiModelProperty("显示的label")
        private String label;
        @ApiModelProperty("参数值")
        @NotBlank(message = "参数值")
        private String value;
        @ApiModelProperty("类型")
        @NotBlank(message = "类型")
        private String kind;
        @ApiModelProperty("类型")
        private Integer sort;
        @ApiModelProperty("创建时间")
        private Date createDate;
    }

    @Data
    public static class querySysPropsRequest  extends BaseProto.RquestPager {
        //@NotBlank(message = "请输入会员编号")
        private String userId;
        //@NotBlank(message = "ip地址")
        private String ipAddress;
        //@NotBlank(message = "请输入 用户类型 1系统用户/ 2团队领导/ 9会员")
        private Integer userType;
        //("开始时间")
        private Date startDate;
        //("结束时间")
        private Date endDate;
        private Integer pageNo;
        private Integer pageSize;
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
    public static class Detele {
        private String id;
    }

    @Data
    public static class Select {
        private String id;
    }
    @Data
    public static class Ids {
        private String ids;
    }
}
