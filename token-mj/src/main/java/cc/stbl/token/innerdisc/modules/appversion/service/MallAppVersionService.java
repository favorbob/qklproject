/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cc.stbl.token.innerdisc.modules.appversion.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.remote.oss.OssProperties;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.appversion.dao.MallAppVersionMapper;
import cc.stbl.token.innerdisc.modules.appversion.entity.MallAppVersion;
import cc.stbl.token.innerdisc.modules.appversion.vo.VersionVo;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import com.stbl.payment.providerImpl.bizorder.bean.ClientType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * App版本Service
 * @author LinJJ
 * @version 2018-04-23
 */
@Service
public class MallAppVersionService extends DataStoreServiceImpl<String,MallAppVersion,MallAppVersionMapper> {

    @Autowired
    private OssProperties ossProperties;

    /**
     * 检查版本更新
     * @param clientVersionCode 客户端版本号
     * @return
     */
    public VersionVo checkVersion(ClientType clientType, Integer clientVersionCode){
        VersionVo versionVo = new VersionVo();
        MallAppVersion newestVersion = this.mapper.getNewestVersion(clientType.name());
        if(newestVersion == null){
            versionVo.setUpdate(false);
        }else{
            Integer newestCode = newestVersion.getVersionCode();
            boolean update = compareVersonCode(clientVersionCode, newestCode);
            versionVo.setUpdate(update);
            if(update){		//当发现新版本时设置其他信息
                versionVo.setNewVersion(newestVersion.getVersionCode());
                versionVo.setUpdateInfo(newestVersion.getUpdateInfo());
                //versionVo.setForceUpdate(newestVersion.getMinVersionCode() != null && clientVersionCode <= newestVersion.getMinVersionCode());
                versionVo.setForceUpdate(false);
                versionVo.setDownloadUrl(newestVersion.getUrl());		//设置admin端下载最新版本请求连接
                versionVo.setSize(newestVersion.getSize());
            }
        }
        return versionVo;
    }

    public MallAppVersion getNewestVersion(ClientType clientType){
        return this.mapper.getNewestVersion(clientType.name());
    }
    /**
     * 对比版本号，检查是否需要更新
     * @param cCode 客户端版本号
     * @param nCode 服务器记录最新版本号
     * @return
     */
    private boolean compareVersonCode(Integer cCode,Integer nCode){
        return nCode > cCode;
    }


    public Integer nextVersionCode(String clientType) {
        return mapper.selectMaxVersionCode(clientType) + 1;
    }

    public void saveOrUpdate(MallAppVersion mallAppVersion){
        String id = mallAppVersion.getId();
        if (StringUtils.isNotEmpty(id)){
            mallAppVersion.setUpdateDate(new Date());
            mallAppVersion.setUpdateBy(ShiroUtils.getLoginUserId());
            super.update(mallAppVersion);
        } else {
            mallAppVersion.setId(JavaUUIDGenerator.get());
            mallAppVersion.setCreateBy(ShiroUtils.getLoginUserId());
            mallAppVersion.setCreateDate(new Date());
            super.add(mallAppVersion);
        }
    }

    public MallAppVersion getById(String id){
        MallAppVersion mallAppVersion = super.get(id);
        if (mallAppVersion == null){
            throw new StructWithCodeException(ResponseCode.DATA_NOT_EXIST);
        }
        String url = mallAppVersion.getUrl();
        if (StringUtils.isNotEmpty(url)){
            if (url.startsWith("http:") || url.startsWith("https:")){
                return mallAppVersion;
            }
            String s = ossProperties.getDefault().getHost() + url;
            mallAppVersion.setUrl(s);
        }
        return mallAppVersion;
    }
}