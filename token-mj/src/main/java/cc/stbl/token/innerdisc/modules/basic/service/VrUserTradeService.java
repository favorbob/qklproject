/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserTradeMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserTrade;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;

@Service
public class VrUserTradeService extends DataStoreServiceImpl<String, VrUserTrade, VrUserTradeMapper> {

    
    @Autowired
    private VrTokenTradeService vrTokenTradeService;
    @Autowired
    private VrUserInfoService vrUserInfoService;
	/**
	 * 转出mj
	 * @param userId
	 * @return
	 */
    @Transactional
	public void transferMJ(VrUserInfo vrUserInfo,String num,String payPassword) {
		String fromUserId = ShiroUtils.getLoginUserId();
    	String toUserId = vrUserInfo.getUserId();
    	BigDecimal amount = new BigDecimal(num);
    	VrUserTrade vrTokenTrade = new VrUserTrade();
    	vrTokenTrade.setId(JavaUUIDGenerator.get());
    	vrTokenTrade.setInAccount(vrUserInfo.getPhoneNum());
    	VrUserInfo outUserInfo = vrUserInfoService.get(fromUserId);
    	vrTokenTrade.setOutAccount(outUserInfo.getPhoneNum());
    	vrTokenTrade.setTradeType(VrUserTrade.TRADE_TYPE_MJ);
    	vrTokenTrade.setStatus(VrUserTrade.TRADE_STATUS_COMPLETE);
    	vrTokenTrade.setTradeNum(new Integer(num));
    	vrTokenTrade.setUpdateTime(new Date());
    	this.add(vrTokenTrade);
    	vrTokenTradeService.transferMJFromExt(payPassword, fromUserId, toUserId, amount, "", "");
	}
    
    
    /**
     * 转AIIC
     */
    public void transferAIIC(VrUserInfo vrUserInfo,String num,String payPassword) {
    	
    	String fromUserId = ShiroUtils.getLoginUserId();
    	String toUserId = vrUserInfo.getUserId();
    	BigDecimal amount = new BigDecimal(num);   	
    	VrUserTrade vrTokenTrade = new VrUserTrade();
    	vrTokenTrade.setId(JavaUUIDGenerator.get());
    	vrTokenTrade.setInAccount(vrUserInfo.getPhoneNum());
    	VrUserInfo outUserInfo = vrUserInfoService.get(fromUserId);
    	vrTokenTrade.setOutAccount(outUserInfo.getPhoneNum());
    	vrTokenTrade.setTradeType(VrUserTrade.TRADE_TYPE_AIIC);
    	vrTokenTrade.setStatus(VrUserTrade.TRADE_STATUS_COMPLETE);
    	vrTokenTrade.setTradeNum(new Integer(num));
    	vrTokenTrade.setUpdateTime(new Date());
    	this.add(vrTokenTrade);
    	vrTokenTradeService.transferFromExt(payPassword, fromUserId, toUserId, amount, "", "");
    	
    	
    }
	
	
   
}