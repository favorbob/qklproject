/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.common.ActivateCardGenerator;
import cc.stbl.token.innerdisc.common.ActivateCardGenerator.ActivateCardType;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserCardMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlowDetail;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCardCount;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;

@Service
public class VrUserCardService extends DataStoreServiceImpl<String, VrUserCard, VrUserCardMapper> {

    @Autowired
    private ActivateCardGenerator activateCardGenerator;
    @Autowired
    private VrUserInfoService vrUserInfoService;
    @Autowired
    private ActivateCardFlowService activateCardFlowService;
    
    @Autowired
    private ActivateCardFlowDetailService activateCardFlowDetailService;
    
    
    /**
     * 
     * @return
     */
    public List<VrUserCard> findByUserIdAndStatus(String userId) {
    	return mapper.findByUserIdAndStatus(userId);
    }
    
    /**
     * 
     * @return
     */
    public List<VrUserCard> selectByUserIdAndStatus(String userId) {
    	return mapper.selectByUserIdAndStatus(userId);
    }
    

    
    public void updateCardByIds(List<String> list,String userId,String phoneNum) {
    	mapper.updateCardByIds(list, userId, phoneNum, new Date());
    }
    
    
    
    /**
     * 
     * @return
     */
    public void updateByIds(Integer status,List<String> userIdList) {
    	mapper.updateByIds(status,userIdList);
    }
    
    
    /**
     * 根据userId查找用户的激活卡量
     * @return
     */
    public Integer countByUserIdAndStatus(String userId) {
    	return mapper.countByUserIdAndStatus(userId);
    }

    /**
     * 查询激活卡统计数据
     * @return
     */
    public VrUserCardCount getVrUserCardCount() {
    	List<Map<String,Object>> vrUserCardCountList =  mapper.groupByCardTypeAndStatus();
    	VrUserCardCount vrUserCardCount = new VrUserCardCount();
    	for(Map map:vrUserCardCountList) {
    		String cardType = map.get("cardType").toString();
    		Integer status = (Integer)map.get("status");
    		Long numTemp = (Long)map.get("num");
    		Integer num = numTemp.intValue();
    		if(cardType.equals(ActivateCardType.GS.get()) && VrUserCard.VR_USER_CARD_STATUS_0.equals(status)) {
    			vrUserCardCount.setCompanyNum(num+(vrUserCardCount.getCompanyNum()==null?0:vrUserCardCount.getCompanyNum()));
    			vrUserCardCount.setCompanyNotUsed(num);
    		}else if(cardType.equals(ActivateCardType.GS.get()) && VrUserCard.VR_USER_CARD_STATUS_1.equals(status)) {
    			vrUserCardCount.setCompanyNum(num+(vrUserCardCount.getCompanyNum()==null?0:vrUserCardCount.getCompanyNum()));
    			vrUserCardCount.setCompanyUse(num);
    		}else if(cardType.equals(ActivateCardType.HY.get()) && VrUserCard.VR_USER_CARD_STATUS_0.equals(status)) {
    			vrUserCardCount.setMemberNum(num+(vrUserCardCount.getMemberNum()==null?0:vrUserCardCount.getMemberNum()));
    			vrUserCardCount.setMemberNotUsed(num);
    		}else if(cardType.equals(ActivateCardType.HY.get()) && VrUserCard.VR_USER_CARD_STATUS_1.equals(status)) {
    			vrUserCardCount.setMemberNum(num+(vrUserCardCount.getMemberNum()==null?0:vrUserCardCount.getMemberNum()));
    			vrUserCardCount.setMemberNumUse(num);
    		}
    	}
    	return vrUserCardCount;
    }
    
    /**
     * 生成卡号
     * @param gs
     * @param num
     */
    @Transactional
    public void saveVrUserCard(String phoneNum,ActivateCardType gs,int num) {    	 
    	Set<String> set = activateCardGenerator.get(gs, num);
    	List<VrUserCard> vrUserCardList = new ArrayList<VrUserCard>();
    	List<ActivateCardFlowDetail> acfdList = new ArrayList<ActivateCardFlowDetail>();
    	String uuid = JavaUUIDGenerator.get();//卡流水id
    	VrUserInfo vrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
    	if(vrUserInfo ==null) {
    		throw new IllegalArgumentException("没找到此电话号码");
    	}
    	for(String cardNo:set) {
    		
    		//用户卡关系
    		VrUserCard vrUserCard = new VrUserCard();
    		vrUserCard.setId(JavaUUIDGenerator.get());
    		vrUserCard.setCardNo(cardNo);
    		vrUserCard.setPhoneNum(phoneNum);
    		vrUserCard.setUpdateTime(new Date());
    		vrUserCard.setStatus(VrUserCard.VR_USER_CARD_STATUS_0);
    		vrUserCard.setCardType(gs.get());
    		vrUserCard.setUserId(vrUserInfo.getUserId());
    		vrUserCardList.add(vrUserCard);
    		
    		//卡流水详情
        	ActivateCardFlowDetail acfd = new ActivateCardFlowDetail();
        	acfd.setActivateCardFlowId(uuid);
        	acfd.setCardNo(cardNo);
        	acfd.setId(JavaUUIDGenerator.get());
        	acfdList.add(acfd);
    	}
    	
    	Integer changeBefore = this.countByUserIdAndStatus(vrUserInfo.getUserId());
    	Integer changeAfter = changeBefore+set.size();
    	ActivateCardFlow entity = new ActivateCardFlow();
    	entity.setCreateTime(new Date());
    	
    	entity.setId(uuid);
    	entity.setChangeBefore(changeBefore);
    	entity.setPhoneNum(phoneNum);
    	entity.setChangeAfter(changeAfter);
    	entity.setChangeNum(set.size());
    	entity.setChangeReason(ActivateCardFlow.ActivateCardFlow_RECHARGE);
    	entity.setRemark("管理员充值激活码账户");
    	//用户卡关系
    	activateCardFlowService.add(entity);
    	//用户卡流水详情
    	activateCardFlowDetailService.addBatch(acfdList);
    	//用户卡流水
    	this.addBatch(vrUserCardList);
    }


}