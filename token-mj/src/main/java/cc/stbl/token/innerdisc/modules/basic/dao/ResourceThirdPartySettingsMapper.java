/**
* generator by mybatis plugin Wed Aug 22 14:23:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdPartySettings;
import com.ks.common.datastore.dao.IDataStoreMapper;

public interface ResourceThirdPartySettingsMapper extends IDataStoreMapper<String, ResourceThirdPartySettings> {

    ResourceThirdPartySettings getByResourceType(Integer resourceType);
}