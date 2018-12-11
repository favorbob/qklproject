/**
* generator by mybatis plugin Wed Aug 22 14:23:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.ResourceThirdBuyingPatternMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdBuyingPattern;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceThirdBuyingPatternService extends DataStoreServiceImpl<String, ResourceThirdBuyingPattern, ResourceThirdBuyingPatternMapper> {

    @Autowired
    private ResourceThirdBuyingPatternMapper mapper;

    public ResourceThirdBuyingPattern selectBySettingsIdAndBuyingPattern(String settingsId, Integer buyingPattern){
        return mapper.selectBySettingsIdAndBuyingPattern(settingsId, buyingPattern);
    }
}