package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class IntroEditProto {

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

    @Data
    public static class IntroEditPro {
        @ApiModelProperty("简介标题")
        @NotBlank(message = "请输入简介标题")
        private String introTitle;
        @ApiModelProperty("简介内容")
        @NotBlank(message = "请输入简介内容")
        private String introContext;
        @ApiModelProperty("附件url")
        private String accessoryUrl;
        @ApiModelProperty("文件名")
        private String fileName;
    }

    @Data
    public static class IntroEditProALL {
        @ApiModelProperty("简介ID")
        @NotBlank(message = "请输入简介ID")
        private String id;
        @ApiModelProperty("简介标题")
        @NotBlank(message = "请输入简介标题")
        private String introTitle;
        @ApiModelProperty("简介内容")
        @NotBlank(message = "请输入简介内容")
        private String introContext;
        @ApiModelProperty("更新时间")
        private Date createDate;
        @ApiModelProperty("备注信息")
        private String remarks;
    }

    @Data
    public static class Id {
        @ApiModelProperty("简介id")
        @NotBlank(message = "请输入简介id")
        private String id;
    }

    @Data
    public static class IntroEditProPage extends BaseProto.RquestPager {
        @ApiModelProperty("简介标题")
        private String introTitle;
        @ApiModelProperty("更新时间")
        private Date createDate;
        @ApiModelProperty("备注信息")
        private String remarks;
    }
    //APP
    @Data
    public static class IntroEditProAPPPage extends BaseProto.RquestPager {
    }

    @Data
    public static class IntroPersonData {
        @ApiModelProperty("系统简介Id")
        @NotBlank(message = "请输入简介id")
        private String introId;
    }
}
