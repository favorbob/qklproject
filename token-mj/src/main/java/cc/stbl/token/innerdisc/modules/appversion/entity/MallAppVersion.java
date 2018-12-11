/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cc.stbl.token.innerdisc.modules.appversion.entity;


import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * App版本Entity
 * @author LinJJ
 * @version 2018-04-23
 */
public class MallAppVersion  {
	private static final long serialVersionUID = 1L;
	private String clientType;		// 客户端类型
	private Integer versionCode;		// 版本号
	private String versionName;		// 版本名
	private Integer minVersionCode;		// 最小版本号
	private String url;		// app地址
	private String updateInfo;		// 更新描述
	private String memo;		// 备注
	private Long size;		// 文件大小
	private String id;
	private Date createDate;
	private Date updateDate;
	private String createBy;
	private String updateBy;



	public MallAppVersion() {
		super();
	}



	@NotNull(message="客户端类型不能为空")
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	@NotNull(message="版本号不能为空")
	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	
	@NotNull(message="版本名不能为空")
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public Integer getMinVersionCode() {
		return minVersionCode;
	}

	public void setMinVersionCode(Integer minVersionCode) {
		this.minVersionCode = minVersionCode;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@NotNull(message="更新描述不能为空")
	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}