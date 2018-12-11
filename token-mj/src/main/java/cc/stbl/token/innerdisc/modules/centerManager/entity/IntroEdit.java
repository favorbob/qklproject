package cc.stbl.token.innerdisc.modules.centerManager.entity;

import java.util.Date;

public class IntroEdit {
    private String id;

    private String introTitle;

    private Date createDate;

    private String remarks;

    private String introContext;

    private String isRead ;  //1-未读，2-已读

    private String accessoryUrl; //附件url地址

    private String fileName; //文件名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIntroContext() {
        return introContext;
    }

    public void setIntroContext(String introContext) {
        this.introContext = introContext;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
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