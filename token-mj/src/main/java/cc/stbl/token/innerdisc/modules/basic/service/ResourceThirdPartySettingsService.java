/**
* generator by mybatis plugin Wed Aug 22 14:23:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.BuyingPatternEnum;
import cc.stbl.token.innerdisc.modules.basic.dao.ResourceThirdPartySettingsMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdBuyingPattern;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdPartySettings;
import cc.stbl.token.innerdisc.restapi.admin.resources.ResourceThirdPartyProto;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ResourceThirdPartySettingsService extends DataStoreServiceImpl<String, ResourceThirdPartySettings, ResourceThirdPartySettingsMapper> {

    @Autowired
    private ResourceThirdPartySettingsMapper mapper;
    @Autowired
    private ResourceThirdBuyingPatternService thirdBuyingPatternService;


    public void saveOrUpdate(ResourceThirdPartyProto.RequestThirdPartySettings thirdPartySettings){
        String settingsId = thirdPartySettings.getSettingsId();
        BigDecimal priceAsset = thirdPartySettings.getPriceAsset();
        BigDecimal priceChange = thirdPartySettings.getPriceChange();
        if (StringUtils.isNotEmpty(settingsId)){
            ResourceThirdPartySettings settings = super.get(settingsId);
            BeanUtils.copyProperties(thirdPartySettings, settings);
            settings.setId(settingsId);
            super.update(settings);


            if (priceAsset != null){
                ResourceThirdBuyingPattern buyingPattern = thirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(settingsId, BuyingPatternEnum.ASSET.getCode());
                buyingPattern.setPrice(priceAsset);
                thirdBuyingPatternService.update(buyingPattern);
            }
            if (priceChange != null){
                ResourceThirdBuyingPattern buyingPattern = thirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(settingsId, BuyingPatternEnum.CHANGE.getCode());
                buyingPattern.setPrice(priceChange);
                thirdBuyingPatternService.update(buyingPattern);
            }

        }else {
            ResourceThirdPartySettings settings = new ResourceThirdPartySettings();
            BeanUtils.copyProperties(thirdPartySettings, settings);
            settings.setId(JavaUUIDGenerator.get());
            super.add(settings);

            ResourceThirdBuyingPattern buyingPattern = new ResourceThirdBuyingPattern();
            BeanUtils.copyProperties(thirdPartySettings, buyingPattern);
            buyingPattern.setSettingsId(settings.getId());
            if (buyingPattern.getPriority() == null){
                buyingPattern.setPriority(0);
            }
            if (priceAsset != null){
                buyingPattern.setId(JavaUUIDGenerator.get());
                buyingPattern.setBuyingPattern(BuyingPatternEnum.ASSET.getCode());
                buyingPattern.setPrice(priceAsset);
                thirdBuyingPatternService.add(buyingPattern);
            }
            if (priceChange != null){
                buyingPattern.setId(JavaUUIDGenerator.get());
                buyingPattern.setBuyingPattern(BuyingPatternEnum.CHANGE.getCode());
                buyingPattern.setPrice(priceChange);
                thirdBuyingPatternService.add(buyingPattern);
            }

        }

    }

    public ResourceThirdPartyProto.ResponseGetThirdPartySettings getThirdPartySettings(Integer resourceType){
        ResourceThirdPartyProto.ResponseGetThirdPartySettings responseGet = new ResourceThirdPartyProto.ResponseGetThirdPartySettings();
        ResourceThirdPartySettings one = mapper.getByResourceType(resourceType);
        if (one == null){
            return null;
        }
        ResourceThirdBuyingPattern buyingPatternAsset = thirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(one.getId(), BuyingPatternEnum.ASSET.getCode());
        ResourceThirdBuyingPattern buyingPatternChange = thirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(one.getId(), BuyingPatternEnum.CHANGE.getCode());
        responseGet.setSettingsId(one.getId());
        responseGet.setFreeUseTime(one.getFreeUseTime());
        responseGet.setResourceType(one.getResourceType());
        if (buyingPatternAsset != null){
            responseGet.setPriceAsset(buyingPatternAsset.getPrice());
            responseGet.setBuyingCategory(buyingPatternAsset.getBuyingCategory());
            responseGet.setPriority(buyingPatternAsset.getPriority());
        }
        if (buyingPatternChange != null){
            responseGet.setPriceChange(buyingPatternChange.getPrice());
            responseGet.setBuyingCategory(buyingPatternChange.getBuyingCategory());
            responseGet.setPriority(buyingPatternChange.getPriority());
        }
        return responseGet;
    }

}