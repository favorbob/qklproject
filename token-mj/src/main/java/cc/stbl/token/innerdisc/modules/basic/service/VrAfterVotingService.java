/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.dao.VrAfterVotingMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrAfterVoting;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;

@Service
public class VrAfterVotingService extends DataStoreServiceImpl<String, VrAfterVoting, VrAfterVotingMapper> {

    @Autowired
    private VrTokenTradeService vrTokenTradeService;
    @Autowired
    private VrTokenManager vrTokenManager;
    @Autowired
    private EthWalletService ethWalletService;
    
    @Autowired
	private VrUserLRValueRecordService vrUserLRValueRecordService;
   
    /**
     * 复投
     * @param userId
     * @param payPassword
     * @param phoneNum
     * @param multiple
     * @param num
     */
    
    @Transactional
	public void afterVoting(String userId,String payPassword,String phoneNum,int multiple,int num) {
		BigDecimal tradeNum = new BigDecimal(multiple*num);
		
		VrAfterVoting afterVoting  = new VrAfterVoting();
		String id = JavaUUIDGenerator.get();
		afterVoting.setId(id);
		afterVoting.setMultiple(multiple);
		afterVoting.setCreateTime(new Date());
		afterVoting.setNum(num);
		afterVoting.setPhoneNum(phoneNum);
		
		EthWallet userWallet = ethWalletService.getUserWallet(userId);
		BigInteger bigInteger = vrTokenManager.integralOf(userWallet.getAddress());
	    BigDecimal bigDecimal = UnitConvertUtils.toEther(bigInteger).setScale(2, RoundingMode.HALF_UP);
        afterVoting.setBeforeAsset(bigDecimal);
        afterVoting.setAfterAsset(bigDecimal.add(tradeNum));
        
        //受限资产
        BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(userWallet.getAddress());

        BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf).setScale(2, RoundingMode.HALF_UP);
        
		if(limitBalanceOfDecimal.intValue() < num) {
			throw new StructWithCodeException(ResponseCode.MJ_NOT_ENOUGH);
		}
        
		//添加复投记录
		this.add(afterVoting);
		
		//记录vr_user_lrvalue_record流水表
		VrUserLRValueRecord vrUserLRValueRecord = new VrUserLRValueRecord();
		vrUserLRValueRecord.setId(id);
		vrUserLRValueRecord.setUserId(userId);
		vrUserLRValueRecord.setStatus(VrUserLRValueRecord.STATUS_0);
		vrUserLRValueRecord.setAddValue(String.valueOf(num));
		vrUserLRValueRecordService.add(vrUserLRValueRecord);
		
		//调用区块链
		vrTokenTradeService.integralAmplifyOf(userId, payPassword, new BigDecimal(num), TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
	}
}