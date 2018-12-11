package cc.stbl.token.innerdisc.restapi.app.card;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlowDetail;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.ActivateCardFlowDetailService;
import cc.stbl.token.innerdisc.modules.basic.service.ActivateCardFlowService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCardService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/card")
@Api(description = "卡模块")
public class CardController {

    public static Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private VrUserInfoService vrUserInfoService;
    
    @Autowired
    private VrUserCardService vrUserCardService;
    
    @Autowired
    private ActivateCardFlowService activateCardFlowService;
    
    @Autowired
    private ActivateCardFlowDetailService activateCardFlowDetailService;

  

    @RequestMapping(value = "/cardGiveEachOther",method = RequestMethod.POST)
    @ApiOperation("激活卡互赠")
    public Response cardGiveEachOther(@RequestBody @Valid CardProto.RequestGiveEachOther request){
  
    	String phoneNum = request.getPhoneNum();
    	String userId = ShiroUtils.getLoginUserId();
    	String payPassword = request.getPayPassword();
    	Integer requstCardNum = request.getCardNum();
        try{
        	//检查用户是否存在
        	VrUserInfo vrUserInfoOthers = vrUserInfoService.findByPhoneNum(phoneNum);
        	if(vrUserInfoOthers == null) {
        		return Response.error("此用户不存在");
        	}
        	
        	Integer status = vrUserInfoOthers.getStatus();
        	if(status != VrUserInfo.USER_STATUS_NORMAL) {
        		return Response.error("此用户状态不正常");
        	}
        	
        	
        	VrUserInfo vrUserInfoMySelf = vrUserInfoService.get(userId);
        	String dbPayPassword = vrUserInfoMySelf.getPayPassword();
        	payPassword = DESUtil.encryptString(payPassword);
        	if(StringUtils.isBlank(payPassword) || StringUtils.isBlank(dbPayPassword) || !payPassword.equals(dbPayPassword)) {
        		return Response.error("支付密码错误");
        	}
        	
        	if(requstCardNum == 0) {
        		return Response.error("输入数量有误");
        	}
        	
        	Integer cardCardNum = vrUserCardService.countByUserIdAndStatus(userId);
        	if(requstCardNum > cardCardNum) {
        		return Response.error("赠送数量大于当前用户拥有的卡数量");
        	}
        	
        	logger.info("userid: 卡激活:{}",userId);
        	List<VrUserCard> vrUserCardList = vrUserCardService.selectByUserIdAndStatus(userId);
        	List<String> vrUserCardIdlist = new ArrayList<String>();
        	List<String> vrUserCardNolist = new ArrayList<String>();
        	logger.info("卡激活数量:{}",vrUserCardList.size());
        	if(vrUserCardList!=null && vrUserCardList.size()>=requstCardNum) {
        		int i=0;
        		for(VrUserCard vrUserCard:vrUserCardList) {
        			vrUserCardIdlist.add(vrUserCard.getId());
        			vrUserCardNolist.add(vrUserCard.getCardNo());
        			i++;
        			if(requstCardNum<=i) {
        				break;
        			}
        		}
        		
        		//1 转出
        		
        		ActivateCardFlow activateCardFlowOut = new ActivateCardFlow();
        		Integer changeBefore = vrUserCardService.countByUserIdAndStatus(userId);
        		Integer changeAfter = changeBefore-requstCardNum;
        		activateCardFlowOut.setChangeBefore(changeBefore);
        		activateCardFlowOut.setChangeAfter(changeAfter);
        		activateCardFlowOut.setChangeNum(requstCardNum);
        		
        		activateCardFlowOut.setChangeReason(ActivateCardFlow.ACTIVATECARDFLOW_OUT);
        		activateCardFlowOut.setCreateTime(new Date());
        		String uuid1 = JavaUUIDGenerator.get();
        		activateCardFlowOut.setId(uuid1);
        		activateCardFlowOut.setPhoneNum(vrUserInfoMySelf.getPhoneNum());
        		activateCardFlowOut.setRemark("转出激活码到"+phoneNum);
        		//记录流水
        		activateCardFlowService.add(activateCardFlowOut);
        		
        		//卡流水详情
        		List<ActivateCardFlowDetail> acfdList1 = new ArrayList<ActivateCardFlowDetail>();
        		for(String cardNo:vrUserCardNolist) {
                	ActivateCardFlowDetail acfd = new ActivateCardFlowDetail();
                	acfd.setActivateCardFlowId(uuid1);
                	logger.info("========cardNo:  "+cardNo);
                	acfd.setCardNo(cardNo);
                	acfd.setId(JavaUUIDGenerator.get());
                	acfdList1.add(acfd);
        		}
        		activateCardFlowDetailService.addBatch(acfdList1);
        		
        		//2转出流水
        		
        		ActivateCardFlow activateCardFlowIn = new ActivateCardFlow();
        		Integer changeBeforeIn = vrUserCardService.countByUserIdAndStatus(vrUserInfoOthers.getUserId());
        		Integer changeAfterIn = changeBeforeIn+requstCardNum;
        		activateCardFlowIn.setChangeBefore(changeBeforeIn);
        		activateCardFlowIn.setChangeAfter(changeAfterIn);
        		activateCardFlowIn.setChangeNum(requstCardNum);
        		activateCardFlowIn.setChangeReason(ActivateCardFlow.ACTIVATECARDFLOW_IN);
        		activateCardFlowIn.setCreateTime(new Date());
        		String uuid2 = JavaUUIDGenerator.get();
        		activateCardFlowIn.setId(uuid2);
        	
        		activateCardFlowIn.setPhoneNum(phoneNum);
        		activateCardFlowIn.setRemark("从"+vrUserInfoMySelf.getPhoneNum()+"转入激活卡");
        		//记录流水
        		activateCardFlowService.add(activateCardFlowIn);
        		
        		//卡流水详情
        		List<ActivateCardFlowDetail> acfdList2 = new ArrayList<ActivateCardFlowDetail>();
        		for(String cardNo:vrUserCardNolist) {
                	ActivateCardFlowDetail acfd = new ActivateCardFlowDetail();
                	acfd.setActivateCardFlowId(uuid2);
                	logger.info("========cardNo:  "+cardNo);
                	acfd.setCardNo(cardNo);
                	acfd.setId(JavaUUIDGenerator.get());
                	acfdList2.add(acfd);
        		}
        		activateCardFlowDetailService.addBatch(acfdList2);
        		vrUserCardService.updateCardByIds(vrUserCardIdlist, vrUserInfoOthers.getUserId(), phoneNum);
        		
        	}else {
        		return Response.error("激活卡数量不够");
        	}
        	return Response.success("赠送成功");
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    
    
    
    @RequestMapping(value = "/cardGiveEachOtherRecord",method = RequestMethod.POST)
    @ApiOperation("获取互赠卡接口")
    public Response cardGiveEachOtherRecord(@RequestBody @Valid CardProto.RequestCardPage request){
  
    	ActivateCardFlow vrUserCardFlow = new ActivateCardFlow();
    	String usreId = ShiroUtils.getLoginUserId();
    	vrUserCardFlow.setChangeReason(ActivateCardFlow.ACTIVATECARDFLOW_OUT);
    	VrUserInfo vrUserInfo = vrUserInfoService.get(usreId);
    	vrUserCardFlow.setPhoneNum(vrUserInfo.getPhoneNum());
        try{
        	Pager<ActivateCardFlow> page = activateCardFlowService.findPage(vrUserCardFlow, request.getPageNo(),request.getPageSize());
        	List<ActivateCardFlow> list = page.getList();
        	for(ActivateCardFlow acf:list) {
        		acf.setPhoneNum(acf.getRemark().split("转出激活码到")[1]);
        		acf.setRemark("赠送"+acf.getChangeNum()+"卡");
        	}
        	return Response.success(page);
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    

    @RequestMapping(value = "/cardNum",method = RequestMethod.POST)
    @ApiOperation("获取卡数量接口")
    public Response cardNum(@RequestBody @Valid CardProto.RequestCardNum request){
  
    	String userId = request.getUserId();
        try{
        	Integer num = vrUserCardService.countByUserIdAndStatus(userId);
        	return Response.success(num);
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
   
}
