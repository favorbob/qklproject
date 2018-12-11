package cc.stbl.token.innerdisc.modules.centerManager.entity;

import java.util.Date;
import java.util.List;

/**
 * @author caojinbo
 * 客服编辑管理
 */
public class CenterEditService {
    private String id;

    private String headLine;

    private String context;

    private Date createDate;

    private String remarks;

    private List<CenterEditServicePic> editServicePics; //客服图片集合

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<CenterEditServicePic> getEditServicePics() {
        return editServicePics;
    }

    public void setEditServicePics(List<CenterEditServicePic> editServicePics) {
        this.editServicePics = editServicePics;
    }
}