package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

/**
* create by framework, create date 2018/08/13 18:19:01
*/
public class VrException {
    /**
    * 字段：vr_exception.log_id；备注：id
     */
    private String logId;

    /**
    * 字段：vr_exception.user_id；备注：user_id
     */
    private String userId;

    /**
    * 字段：vr_exception.drive_id；备注：设备id
     */
    private String driveId;

    /**
    * 字段：vr_exception.os_version；备注：系统版本
     */
    private String osVersion;

    /**
    * 字段：vr_exception.os_name；备注：系统名称
     */
    private String osName;

    /**
    * 字段：vr_exception.client_version；备注：客户端版本
     */
    private String clientVersion;

    /**
    * 字段：vr_exception.function_name；备注：功能名
     */
    private String functionName;

    /**
    * 字段：vr_exception.net_info；备注：网络格式0:WIFI,1:4G,2:3G,3:2G
     */
    private Integer netInfo;

    /**
    * 字段：vr_exception.create_date；备注：上传时间
     */
    private Date createDate;

    /**
    * 字段：vr_exception.log_detail；备注：错误内容
     */
    private String logDetail;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getNetInfo() {
        return netInfo;
    }

    public void setNetInfo(Integer netInfo) {
        this.netInfo = netInfo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }
}