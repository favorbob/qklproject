package cc.stbl.token.innerdisc.modules.basic.dto;

import cc.stbl.token.innerdisc.modules.basic.entity.ResourceInfo;

import java.util.Date;

/**
* create by Kael Chan, create date 2018/08/21 11:27:12
*/
public class ResourceInfoDTO extends ResourceInfo {

    // 开始时间
    private Date startTime;

   // 结束时间
    private Date endTime;

    private Integer startRow;

    private Integer pageSize;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}