package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.enums.BuyingCategoryEnum;
import cc.stbl.token.innerdisc.common.enums.BuyingPatternEnum;
import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdBuyingPattern;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceThirdPartySettings;
import cc.stbl.token.innerdisc.restapi.admin.resources.ResourceThirdPartyProto;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ResourceThirdBuyingPatternServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceThirdBuyingPatternService resourceThirdBuyingPatternService;
    @Autowired
    private ResourceThirdPartySettingsService resourceThirdPartySettingsService;

    @Test
    public void saveOrUpdate() {

    }
}