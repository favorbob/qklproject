package cc.stbl.token.innerdisc.modules.appversion.vo;

/**
 * Created by Developer on 2017-3-2.
 */
public class VersionVo {
    private boolean update;         //是否发现新版本
    private long newVersion;     //新版本版本号
    private boolean forceUpdate;          // 是否强制更新（0-非强制，1-强制）
    private String updateInfo;          //更新说明
    private String downloadUrl;             //下载地址（访问admin项目下载最新版本）
    private Long size;                  //文件大小（单位byte）

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(long newVersion) {
        this.newVersion = newVersion;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String toString(){
        String s = "update="+ isUpdate() + ",newVersion="+getNewVersion()+",lastForce="+forceUpdate+",updateInfo="+getUpdateInfo()+",downloadUrl="+getDownloadUrl();
        return s;
    }

}
