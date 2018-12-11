package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import cc.stbl.token.innerdisc.restapi.BaseProto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class NoticeEditProto {

    @Data
    public static class NoticeEditPro {
        @ApiModelProperty("公告标题")
        @NotBlank(message = "请输入公告标题")
        private String noticeTitle;
        @ApiModelProperty("公告内容")
        private String noticeContext;
        @ApiModelProperty("是否弹窗")
        @NotNull(message = "请输入消息类型")
        private Integer isPop;
        @ApiModelProperty("消息类型")
        @NotNull(message = "请输入消息类型")
        private Integer msgType;
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String consigneeNum;
        @ApiModelProperty("弹屏图片url")
        private String popPicUrl;
        @ApiModelProperty("附件url")
        private String accessoryUrl;
        @ApiModelProperty("文件名")
        private String fileName;
    }

    @Data
    public static class NoticeEditProALL {
        @ApiModelProperty("公告ID")
        @NotBlank(message = "请输入公告ID")
        private String id;
        @ApiModelProperty("公告标题")
        @NotBlank(message = "请输入公告标题")
        private String noticeTitle;
        @ApiModelProperty("公告内容")
        @NotBlank(message = "请输入公告内容")
        private String noticeContext;
        @ApiModelProperty("是否弹窗")
        @NotNull(message = "请输入是否弹窗")
        private int isPop;
        @ApiModelProperty("消息类型")
        @NotNull(message = "请输入消息类型")
        private Integer msgType;
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String consigneeNum;
    }

    @Data
    public static class IdAndNum {
        @ApiModelProperty("简介公告id")
        @NotBlank(message = "请输入客服图片id")
        private String id;
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String consigneeNum ;
    }

    @Data
    public static class NoticeEditProPage extends BaseProto.RquestPager {
        @ApiModelProperty("公告标题")
        private String noticeTitle;
        @ApiModelProperty("公告内容")
        private String noticeContext;
    }
    @Data
    public static class NoticeEditNullPage extends BaseProto.RquestPager {

    }
    @Data
    public static class NoticeUserPro {
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String consigneeNum;
        @ApiModelProperty("用户id")
        @NotBlank(message = "请输入用户id，多个用,分割")
        private String userId;
        @ApiModelProperty("姓名")
        @NotBlank(message = "请输入姓名，多个用,分割")
        private String userName;
    }
    @Data
    public static class ConsigneeNum {
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String consigneeNum;
    }
    @Data
    public static class Id {
        @ApiModelProperty("公告id")
        @NotBlank(message = "请输入公告id")
        private String id;
    }
    @Data
    public static class UserId {
        @ApiModelProperty("用户id")
        @NotBlank(message = "请输入用户id")
        private String userId;
    }
    @Data
    public static class NoticeConNum {
        @ApiModelProperty("收件人编号")
        @NotBlank(message = "请输入收件人编号")
        private String noticeConsignee;
    }

    @Data
    public static class NoticePersonData {
        @ApiModelProperty("系统公告Id")
        @NotBlank(message = "请输入系统公告id")
        private String noticeId;
    }
    @Data
    public static class MsgType {
        @ApiModelProperty("系统公告Id")
        private String msgType;
    }
}
