package cc.stbl.token.innerdisc.restapi.admin.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserAssetUpdateLog;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserAssetUpdateLogService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserCardService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 奖金明细查询Controller
 * @author caojinbo
 * @version 2018-09-27
 */

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/base/varusermaintain")
@Api(description = "查询/会员账户维护")
public class VrUserInfoFlowController {

	@Autowired
	private VrUserInfoService vrUserInfoService;
	
	@Autowired
	private VrUserCardService vrUserCardService;
	
	 @Autowired
	private VrTokenManager vrTokenManager;
	 
	@Autowired
	private EthWalletService ethWalletService;
	
	@Autowired
	private VrUserAssetUpdateLogService vrUserAssetUpdateLogService;


	//根据分页 奖金明细查询
	@RequestMapping(value = {"/getAssetPage"},method = RequestMethod.POST)
	@ApiOperation("会员账户维护查询list")
	public Response<Pager<VrUserInfo>> getAssetPage(@RequestBody VrUserInfoFlowProto.RequestQuery condition) {
		VrUserInfo object = new VrUserInfo();
		BeanUtils.copyProperties(condition, object);
		Pager<VrUserInfo> page = vrUserInfoService.findUserAccountByPhone(object, condition.getPageNo(), condition.getPageSize());
		
		List<VrUserInfo> vrUserInfoList = page.getList();
		for(VrUserInfo vrUserInfo:vrUserInfoList) {
			String userId = vrUserInfo.getUserId();
			
			Integer activeCode = vrUserCardService.countByUserIdAndStatus(userId);
			//激活卡
			vrUserInfo.setActiveCode(activeCode.toString());
			
			EthWallet userWallet = ethWalletService.getUserWallet(userId);
			if(userWallet == null) {
				continue;
			}
			
			  //mj
	        BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(userWallet.getAddress());
	        BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf).setScale(2, RoundingMode.HALF_UP);
	        vrUserInfo.setMjAccount(limitBalanceOfDecimal.toString());
	        
	        //aiic
	        BigInteger balanceOf = vrTokenManager.balanceOf(userWallet.getAddress());
	        BigDecimal balanceOfDecimal = UnitConvertUtils.toEther(balanceOf).setScale(2, RoundingMode.HALF_UP);
	        vrUserInfo.setAiicAccount(balanceOfDecimal.toString());

	        //资产
	        BigInteger bigInteger = vrTokenManager.integralOf(userWallet.getAddress());
	        BigDecimal bigDecimal = UnitConvertUtils.toEther(bigInteger).setScale(2, RoundingMode.HALF_UP);
	        vrUserInfo.setPropertyAccount(bigDecimal.toString());
		}
		
		return Response.success(page);
	}
	
	
	
	    //会员账户资金修改列表
		@RequestMapping(value = {"/getAssetDetailPage"},method = RequestMethod.POST)
		@ApiOperation("会员账户资金修改列表")
		public Response<Pager<VrUserAssetUpdateLog>> getVrprizeDetailPage(@RequestBody VrUserInfoFlowProto.RequestAssetDetailList condition) {
			VrUserAssetUpdateLog object = new VrUserAssetUpdateLog();
			BeanUtils.copyProperties(condition, object);
			object.desc("createTime");
			Pager<VrUserAssetUpdateLog> page = vrUserAssetUpdateLogService.findPage(object, condition.getPageNo(), condition.getPageSize());
			
			return Response.success(page);
		}
	
	@PostMapping("/update")
	public Response update(@RequestBody VrUserInfoFlowProto.RequestUpdate request) {
		String phoneNum = request.getPhoneNum();
		String userId = request.getUserId();
		String value = request.getValue();
		Integer type = request.getType();
		vrUserAssetUpdateLogService.add(phoneNum,userId,value,type);
		return Response.success("资产修改成功");
	}

}
