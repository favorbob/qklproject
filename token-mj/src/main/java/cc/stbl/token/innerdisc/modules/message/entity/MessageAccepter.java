package cc.stbl.token.innerdisc.modules.message.entity;

import ss.stbl.common.datastore.domain.OrderByQuery;

import java.util.Date;

/**
* create by framework, create date 2018/08/23 22:27:26
*/
public class MessageAccepter extends OrderByQuery {
    /**
    * 字段：message_accepter.id；备注：编号
     */

    private String id;

    /**
    * 字段：message_accepter.message_id；备注：消息id
     */
    private String messageId;

    /**
    * 字段：message_accepter.accepter_id；备注：接收人id
     */
    private String accepterId;

    /**
    * 字段：message_accepter.status；备注：发送状态
     */
    private Integer status;

    /**
    * 字段：message_accepter.send_date；备注：发送日期
     */
    private Date sendDate;

    /**
    * 字段：message_accepter.create_by；备注：创建者
     */
    private String createBy;

    /**
    * 字段：message_accepter.create_date；备注：创建时间
     */
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
    private Integer messageType;

    /**
    * 字段：message_accepter.check_status；备注：0已读1未读
     */
    private Integer checkStatus;

    /**
    * 字段：message_accepter.title；备注：标题
     */
    private String title;

    /**
    * 字段：message_accepter.content；备注：内容
     */
    private String content;

    /**
    * 字段：message_accepter.accepter_name；备注：接收人名
     */
    private String accepterName;

    /**
    * 字段：message_accepter.extend_id；备注：扩展ID
     */
    private String extendId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAccepterId() {
        return accepterId;
    }

    public void setAccepterId(String accepterId) {
        this.accepterId = accepterId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccepterName() {
        return accepterName;
    }

    public void setAccepterName(String accepterName) {
        this.accepterName = accepterName;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }
}