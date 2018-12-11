package cc.stbl.token.innerdisc.modules.centerManager.entity;

import java.util.Date;

/**
 * @author caojinbo
 */
public class CenterHomePic {
    private String id;

    private String picUrl;

    private String picName;

    private String outUrl;

    private Date createDate;

    private String remarks;

    private Integer picSort;  //首页排序字段

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
    }

    public Integer getPicSort() {
        return picSort;
    }

    public void setPicSort(Integer picSort) {
        this.picSort = picSort;
    }
}