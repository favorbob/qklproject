package cc.stbl.token.innerdisc.restapi.app.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class MessageProto {

    @Data
    public static class MessageRequestSupports{
        @NotBlank(message = "接收人Id")
        private String accepterId;//接收人Id
        @NotBlank(message = "资产ID")
        private String extendId;//扩展ID
    }

    @Data
    public static class MessageId{
        @NotBlank(message = "消息ID")
        private String id;
    }

    @Data
    public static class ResponseMessage{
        @ApiModelProperty("消息id")
        private String id;

        private String messageId;

        @ApiModelProperty("接收人id")
        private String accepterId;

        @ApiModelProperty("发送状态")
        private Integer status;

        /**
         * 字段：message_accepter.send_date；备注：发送日期
         */
        @ApiModelProperty("发送日期")
        private Date sendDate;

        /**
         * 字段：message_accepter.create_by；备注：创建者
         */
        @ApiModelProperty("发送人")
        private String createBy;

        /**
         * 字段：message_accepter.create_date；备注：创建时间
         */
        @ApiModelProperty("创建时间")
        private Date createDate;

        /**
         * 字段：message_accepter.update_by；备注：更新者
         */
        private String updateBy;

        /**
         * 字段：message_accepter.update_date；备注：更新时间
         */
        private Date updateDate;

        /**
         * 字段：message_accepter.remarks；备注：备注信息
         */
        private String remarks;

        /**
         * 字段：message_accepter.del_flag；备注：删除标记
         */
        private String delFlag;

        /**
         * 字段：message_accepter.message_type；备注：消息类型//0系统消息1资产消息
         */
        @ApiModelProperty("消息类型//0系统消息1资产消息")
        private Integer messageType;

        /**
         * 字段：message_accepter.check_status；备注：0已读1未读
         */
        @ApiModelProperty("查看消息状态//0已读1未读")
        private Integer checkStatus;

        /**
         * 字段：message_accepter.title；备注：标题
         */
        @ApiModelProperty("标题")
        private String title;

        /**
         * 字段：message_accepter.content；备注：内容
         */
        @ApiModelProperty("内容")
        private String content;

        /**
         * 字段：message_accepter.accepter_name；备注：接收人名
         */
        @ApiModelProperty("接收人名")
        private String accepterName;

        /**
         * 字段：message_accepter.extend_id；备注：扩展ID
         */
        @ApiModelProperty("资产ID")
        private String extendId;
    }

}
