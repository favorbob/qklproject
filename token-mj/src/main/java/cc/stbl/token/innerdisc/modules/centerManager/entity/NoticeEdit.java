package cc.stbl.token.innerdisc.modules.centerManager.entity;

import java.util.Date;

public class NoticeEdit {
    private String id;

    private String noticeTitle;

    private String consigneeNum;

    private Integer statue;

    private Integer isPop;

    private Integer msgType;

    private Date createDate;

    private Date updateDate;

    private String remarks;

    private String noticeContext;

    private String isRead ;

    private String popPicUrl;

    private String accessoryUrl; //附件url地址

    private String fileName; //文件名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getConsigneeNum() {
        return consigneeNum;
    }

    public void setConsigneeNum(String consigneeNum) {
        this.consigneeNum = consigneeNum;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Integer getIsPop() {
        return isPop;
    }

    public void setIsPop(Integer isPop) {
        this.isPop = isPop;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getNoticeContext() {
        return noticeContext;
    }

    public void setNoticeContext(String noticeContext) {
        this.noticeContext = noticeContext;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getPopPicUrl() {
        return popPicUrl;
    }

    public void setPopPicUrl(String popPicUrl) {
        this.popPicUrl = popPicUrl;
    }

    public String getAccessoryUrl() {
        return accessoryUrl;
    }

    public void setAccessoryUrl(String accessoryUrl) {
        this.accessoryUrl = accessoryUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}