package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class CenterHomePicProto {

    @Data
    public static class PicRequestList extends BaseProto.RquestPager {
        @ApiModelProperty("图片ID")
        private String id;
        @ApiModelProperty("图片url")
        private String picUrl;
        @ApiModelProperty("链接")
        private String outUrl;
        @ApiModelProperty("创建时间")
        private Date createDate;
    }

    //app page
    @Data
    public static class PicRequestListAddPage extends BaseProto.RquestPager {
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
    public static class OutUrl {
        @ApiModelProperty("超链接")
        private String outUrl;
    }
    @Data
    public static class PicId {
        @ApiModelProperty("图片id")
        @NotBlank(message = "请输入图片id")
        private String picId;
    }
    @Data
    public static class IdSorts {
        @ApiModelProperty("图片Id集合")
        @NotBlank(message = "请输入图片Id集合")
        private String ids;
        @ApiModelProperty("排序集合")
        @NotBlank(message = "请输入排序集合")
        private String picSorts;
    }
}
