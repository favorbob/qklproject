/**
* generator by mybatis plugin Wed Aug 22 14:23:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.dao;

import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdBuyingPattern;
import com.ks.common.datastore.dao.IDataStoreMapper;
import org.apache.ibatis.annotations.Param;

public interface ResourceThirdBuyingPatternMapper extends IDataStoreMapper<String, ResourceThirdBuyingPattern> {

    ResourceThirdBuyingPattern selectBySettingsIdAndBuyingPattern(@Param("settingsId")String settingsId, @Param("buyingPattern")Integer buyingPattern);
}