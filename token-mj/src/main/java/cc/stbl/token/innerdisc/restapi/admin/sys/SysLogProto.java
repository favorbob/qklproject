package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class SysLogProto {

    @Data
    public static class AddLogRequest {
        @NotBlank(message = "请输入会员编号")
        private String userId;
        @NotBlank(message = "请输入姓名")
        private String userName;
        @NotBlank(message = "请输入IP地址")
        private String ipAddress;
        @NotBlank(message = "请输入日志信息")
        private String logInfo;
        @ApiModelProperty("日志类型,1-系统日志，2-操作日志")
        @NotNull(message = "请输入日志类型")
        private Integer isSysLog;
        @ApiModelProperty("用户类型：1-管理员，2-会员")
        @NotNull(message = "请输入用户类型")
        private Integer userType;
    }

    @Data
    public static class querySysLogInfoRequest {
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
}
