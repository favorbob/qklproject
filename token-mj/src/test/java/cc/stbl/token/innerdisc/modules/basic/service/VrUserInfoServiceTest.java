package cc.stbl.token.innerdisc.modules.basic.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfoAttribute;
import cc.stbl.token.innerdisc.restapi.app.user.UserProto;
import net.sf.json.JSON;

public class VrUserInfoServiceTest extends AbstractTestCase {

	@Autowired
	private VrUserInfoAttributeService aService;

    @Autowired
    private VrUserInfoService vrUserInfoService;

	@Test
	public void testGetVrUserInfoAttribute() {
		VrUserInfoAttribute vrUserInfoAttribute = aService.getVrUserInfoAttribute("80c5f203011a497299911f4347b7c626");
		System.out.println("======" + ToStringBuilder.reflectionToString(vrUserInfoAttribute));
	}

    @Test
    public void getMyPromotionInfo() {
        System.out.println("VrUserInfoServiceTest.getMyPromotionInfo ---> start");
        String userId = "c152bed5ddb341c485c0a89100727a3f";
        UserProto.RequestMyPromotion requestMyPromotion = new UserProto.RequestMyPromotion();
        requestMyPromotion.setPageNo(1);
        requestMyPromotion.setPageSize(100);
        UserProto.ResponseMyPromotion myPromotionInfo = vrUserInfoService.getMyPromotionInfo(userId, requestMyPromotion);
        List<UserProto.ResponseSubInfo> lowerInfoList = myPromotionInfo.getLowerInfoList();
        for (UserProto.ResponseSubInfo info : lowerInfoList){
            System.out.println("VrUserInfoServiceTest.getMyPromotionInfo --->" + info);
        }
        System.out.println("VrUserInfoServiceTest.getMyPromotionInfo ---> end");
    }

    @Test
    public void genUserReceiveCode(){
        String url = vrUserInfoService.genUserReceiveCode("https://www.baidu.com/");
        System.out.println("VrUserInfoServiceTest.getMyPromotionInfo --->" + url);
    }

	@Test
	public void testGetPrizeUserList() {
		List<VrUserInfo> prizeUserList = vrUserInfoService.getPrizeUserList(2, LocalDate.now().toString());
		System.out.println("==========prize:");
		prizeUserList.stream().forEach(u -> System.out.println(ToStringBuilder.reflectionToString(u)));

		List<VrUserInfo> subLevelUserList = vrUserInfoService.getSubLevelUserList("1_A", 2, 20);
		System.out.println("============sub:");
		subLevelUserList.stream().forEach(u -> System.out.println(ToStringBuilder.reflectionToString(u)));
	}

}