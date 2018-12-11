/**
* generator by mybatis plugin Wed Jul 18 09:07:54 GMT+08:00 2018
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
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserAssetUpdateLogMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserAssetUpdateLog;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;

/**
 * 账号维护
 * @author fyf
 *
 */
@Service
public class VrUserAssetUpdateLogService
		extends DataStoreServiceImpl<String, VrUserAssetUpdateLog, VrUserAssetUpdateLogMapper> {
	@Autowired
	private VrTokenManager vrTokenManager;

	@Autowired
	private VrTokenTradeService vrTokenTradeService;

	@Autowired
	private EthWalletService ethWalletService;

	@Autowired
	private VrUserInfoService vrUserInfoService;

	@Transactional
	public VrUserAssetUpdateLog add(String phoneNum,String userId,String afterValue, Integer assetType) {

		EthWallet userWallet = ethWalletService.getUserWallet(userId);
		if (userWallet == null) {
			throw new StructWithCodeException(ResponseCode.NO_WALLET);
		}

		BigDecimal afterValueBigDecimal = new BigDecimal(afterValue);
		String beforeValue = "";
		switch (assetType) {
		case 1:
			// mj
			BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(userWallet.getAddress());
			BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf).setScale(2,
					RoundingMode.HALF_UP);
//			int result = afterValueBigDecimal.compareTo(limitBalanceOfDecimal);
//			if (result == -1) {
//
//			} else if (result == 1) {
//
//			}
			vrTokenTradeService.restAsset(userId, afterValueBigDecimal, JavaUUIDGenerator.get());
			beforeValue = limitBalanceOfDecimal.toString();
			break;

		case 2:
			// aiic
			BigInteger balanceOf = vrTokenManager.balanceOf(userWallet.getAddress());
			BigDecimal balanceOfDecimal = UnitConvertUtils.toEther(balanceOf).setScale(2, RoundingMode.HALF_UP);
//			result = afterValueBigDecimal.compareTo(balanceOfDecimal);
//			if (result == -1) {
//				vrTokenTradeService.deductMj(userId, BEnum.DEDUCT, balanceOfDecimal.subtract(afterValueBigDecimal),
//						null);
//			} else if (result == 1) {
//				vrTokenTradeService.chargeAssetMj(userId, BEnum.SYS_CHARGE,
//						afterValueBigDecimal.subtract(balanceOfDecimal), null);
//			}

			vrTokenTradeService.restMj(userId, afterValueBigDecimal, JavaUUIDGenerator.get());
			beforeValue = balanceOfDecimal.toString();
			break;
		case 3:
			// 资产
			BigInteger bigInteger = vrTokenManager.integralOf(userWallet.getAddress());
			BigDecimal assetDecimal = UnitConvertUtils.toEther(bigInteger).setScale(2, RoundingMode.HALF_UP);
//			result = afterValueBigDecimal.compareTo(assetDecimal);
//			if (result == -1) {
//				vrTokenTradeService.deductAsset(userId, BEnum.DEDUCT, assetDecimal.subtract(afterValueBigDecimal),
//						null);
//			} else if (result == 1) {
//				vrTokenTradeService.chargeAsset(userId, BEnum.SYS_CHARGE, afterValueBigDecimal.subtract(assetDecimal),
//						null);
//			}
			vrTokenTradeService.restAiic(userId, afterValueBigDecimal, JavaUUIDGenerator.get());

			beforeValue = assetDecimal.toString();
			break;
		}

		// 添加修改流水
		VrUserAssetUpdateLog entity = new VrUserAssetUpdateLog();
		entity.setId(JavaUUIDGenerator.get());
		entity.setAssetChangeAfter(afterValue);
		entity.setAssetChangeBefore(beforeValue);
		entity.setAssetType(assetType);
		entity.setCreateTime(new Date());
		entity.setPhoneNum(phoneNum);
		entity.setUserId(userId);
		this.add(entity);

		return null;
	}
}