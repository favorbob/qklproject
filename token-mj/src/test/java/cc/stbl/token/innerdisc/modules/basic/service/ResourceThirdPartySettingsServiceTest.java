package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.enums.BuyingCategoryEnum;
import cc.stbl.token.innerdisc.common.enums.BuyingPatternEnum;
import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdBuyingPattern;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdPartySettings;
import cc.stbl.token.innerdisc.restapi.admin.resources.ResourceThirdPartyProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class ResourceThirdPartySettingsServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceThirdBuyingPatternService resourceThirdBuyingPatternService;
    @Autowired
    private ResourceThirdPartySettingsService resourceThirdPartySettingsService;

    @Test
    public void saveOrUpdate() {
        ResourceThirdPartyProto.RequestThirdPartySettings thirdPartySettings = new ResourceThirdPartyProto.RequestThirdPartySettings();
        thirdPartySettings.setBuyingCategory(BuyingCategoryEnum.GAME_BEFORE_UNLOCKING.getCode());
        thirdPartySettings.setFreeUseTime(300);
        thirdPartySettings.setPriceAsset(new BigDecimal(9.99));
        thirdPartySettings.setPriceChange(new BigDecimal(100));
        thirdPartySettings.setPriority(0);
        thirdPartySettings.setResourceType(ResourceTypeEnum.GAME.getCode());
        resourceThirdPartySettingsService.saveOrUpdate(thirdPartySettings);
        thirdPartySettings.setResourceType(ResourceTypeEnum.VIDEO.getCode());
        resourceThirdPartySettingsService.saveOrUpdate(thirdPartySettings);

        ResourceThirdPartyProto.ResponseGetThirdPartySettings settings = resourceThirdPartySettingsService.getThirdPartySettings(ResourceTypeEnum.GAME.getCode());
        ResourceThirdPartyProto.ResponseGetThirdPartySettings settings2 = resourceThirdPartySettingsService.getThirdPartySettings(ResourceTypeEnum.VIDEO.getCode());
        thirdPartySettings.setSettingsId(settings.getSettingsId());
        thirdPartySettings.setPriceAsset(new BigDecimal(1.11));
        thirdPartySettings.setPriceChange(new BigDecimal(200));
        resourceThirdPartySettingsService.saveOrUpdate(thirdPartySettings);
        ResourceThirdBuyingPattern buyingPattern = resourceThirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(settings.getSettingsId(), BuyingPatternEnum.CHANGE.getCode());
        ResourceThirdBuyingPattern buyingPattern1 = resourceThirdBuyingPatternService.selectBySettingsIdAndBuyingPattern(settings.getSettingsId(), BuyingPatternEnum.ASSET.getCode());
        System.out.println("ResourceThirdBuyingPatternServiceTest.saveOrUpdate --->" + settings);
        System.out.println("ResourceThirdBuyingPatternServiceTest.saveOrUpdate --->" + buyingPattern);
        System.out.println("ResourceThirdBuyingPatternServiceTest.saveOrUpdate --->" + buyingPattern1);
    }

    @Test
    public void getThirdPartySettings(){
        ResourceThirdPartyProto.ResponseGetThirdPartySettings settings = resourceThirdPartySettingsService.getThirdPartySettings(ResourceTypeEnum.GAME.getCode());
        ResourceThirdPartyProto.ResponseGetThirdPartySettings settings2 = resourceThirdPartySettingsService.getThirdPartySettings(ResourceTypeEnum.VIDEO.getCode());
        System.out.println("ResourceThirdBuyingPatternServiceTest.getThirdPartySettings --->" + settings);
        System.out.println("ResourceThirdBuyingPatternServiceTest.getThirdPartySettings --->" + settings2);
    }
}