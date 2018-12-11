package cc.stbl.token.innerdisc.modules.basic.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 设备使用时长记录DTO
 */
@ApiModel
public class DeviceUseTimeDTO {
    @ApiModelProperty("资源ID")
    private String resourceId;
    @ApiModelProperty("资源名称")
    private String resourceName;
    @ApiModelProperty("资源类型")
    private Integer resourceType;
    @ApiModelProperty("观看时长")
    private Long sumTimes;
    @ApiModelProperty("最后观看时间")
    private Date maxEndTime;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Long getSumTimes() {
        return sumTimes;
    }

    public void setSumTimes(Long sumTimes) {
        this.sumTimes = sumTimes;
    }

    public Date getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(Date maxEndTime) {
        this.maxEndTime = maxEndTime;
    }
}
