package cc.stbl.token.innerdisc.modules.payment.service;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.enums.AssetTypeEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthAssetFlow;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthAssetFlowService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.TradeConsts;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.modules.eth.trades.accelerate.AbstractIntegralAdapter;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.payment.MyAssetsProto;

import com.ks.common.datastore.exception.StructWithCodeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"MyAssetsService"})
public class MyAssetsService {

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private EthWalletService ethWalletService;

    @Autowired
    private EthTradeRecordService ethTradeRecordService;

    @Autowired
    private EthAssetFlowService ethAssetFlowService;

    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private AbstractIntegralAdapter abstractIntegralAdapter;

    @Autowired
    private VrUserInfoService vrUserInfoService;

//    public static String userId = ShiroUtils.getLoginUserId();

	// 我的资产首页
	public Response<MyAssetsProto.ResponseMyAssetsSupports> myAssetsHome() {
		String userId = ShiroUtils.getLoginUserId();
		return myAssetsHome(userId);
	}

	// @Cacheable(key = "'user:vr:myAssetsHome:id_' + #userId")
	// @CacheExpiration(expire = 3 * 60 * 60)
	public Response<MyAssetsProto.ResponseMyAssetsSupports> myAssetsHome(String userId) {
		logger.debug("=========Response<MyAssetsProto.ResponseMyAssetsSupports> myAssetsHome({})", userId);
        MyAssetsProto.ResponseMyAssetsSupports response = new MyAssetsProto.ResponseMyAssetsSupports();
        //当前释放比例
        BigDecimal bigDecimal1 = abstractIntegralAdapter.readRebateRatio(userId);
        VrUserInfo vrUserInfo = vrUserInfoService.get(userId);
        EthWallet userWallet = ethWalletService.getUserWallet(userId);
        if(userWallet == null ){
            response.setAvailableAssets("0");
            response.setRestrictedAssets("0");
            response.setTotalAssets("0");
            response.setIntegral("0");
            response.setTranslateAssets("0");
            response.setReleaseRatio(bigDecimal1);
            response.setWalletAddress("");
            response.setAArea(vrUserInfo.getaArea());
            response.setBArea(vrUserInfo.getbArea());
            return Response.success(response);
        }
        //受限资产
        BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(userWallet.getAddress());

        BigDecimal limitBalanceOfDecimal = UnitConvertUtils.toEther(limitBalanceOf).setScale(2, RoundingMode.HALF_UP);
        response.setRestrictedAssets(limitBalanceOfDecimal.toString());
        //可用资产
        BigInteger balanceOf = vrTokenManager.balanceOf(userWallet.getAddress());
        BigDecimal balanceOfDecimal = UnitConvertUtils.toEther(balanceOf).setScale(2, RoundingMode.HALF_UP);
        response.setAvailableAssets(balanceOfDecimal.toString());

        //冻结资产
        BigInteger lockedBalanceOf = vrTokenManager.lockedBalanceOf(userWallet.getAddress());
        BigDecimal lockedBalanceOfDecimal = UnitConvertUtils.toEther(lockedBalanceOf).setScale(2, RoundingMode.HALF_UP);
        response.setTranslateAssets(lockedBalanceOfDecimal.toString());

        //积分
        BigInteger bigInteger = vrTokenManager.integralOf(userWallet.getAddress());
        BigDecimal bigDecimal = UnitConvertUtils.toEther(bigInteger).setScale(2, RoundingMode.HALF_UP);
        response.setIntegral(bigDecimal.toString());

        response.setTotalAssets(UnitConvertUtils.toEther(vrTokenManager.totalBalanceOf(userWallet.getAddress())).setScale(2, RoundingMode.HALF_UP).toString());
        response.setWalletAddress(userWallet.getAddress());

//        BigDecimal bigDecimal1 = UnitConvertUtils.toEther(bigInteger1);
        response.setReleaseRatio(bigDecimal1);

        
        response.setAArea(vrUserInfo.getaArea());
        response.setBArea(vrUserInfo.getbArea());
        
        return Response.success(response);
    }

    //交易记录
    public List<EthTradeRecord> transactionRecord(String userId){
        EthTradeRecord ethTradeRecord = new EthTradeRecord();
        ethTradeRecord.setFromUserId(userId);
        ethTradeRecord.setToUserId(userId);
        return ethTradeRecordService.unionAllUserID(ethTradeRecord);
    }

    //转出记录
    public List<EthTradeRecord> turnOutRecord(String userId){
        EthTradeRecord ethTradeRecord = new EthTradeRecord();
        ethTradeRecord.setFromUserId(userId);
        ethTradeRecord.setTradeType(TradeConsts.TRADE_TYPE_TRANSFER);
        return ethTradeRecordService.findList(ethTradeRecord);
    }
    //转出记录
    public List<EthTradeRecord> turnInRecord(String userId){
        EthTradeRecord ethTradeRecord = new EthTradeRecord();
        ethTradeRecord.setToUserId(userId);
        ethTradeRecord.setTradeType(TradeConsts.TRADE_TYPE_TRANSFER);
        return ethTradeRecordService.findList(ethTradeRecord);
    }

    //资产明细
    public List<EthAssetFlow> myAssetsDetail(Integer balanceType){
        String userId = ShiroUtils.getLoginUserId();
        EthAssetFlow ethAssetFlow = new EthAssetFlow();
        ethAssetFlow.setUserId(userId);
        ethAssetFlow.setTradeType(balanceType);
        return ethAssetFlowService.findList(ethAssetFlow);
    }

    //交易详情
    public EthTradeRecord transactionDetail(String id){
        return ethTradeRecordService.get(id);
    }

    //兑换为积分
    public void integralAmplifyOf(String userId, String payPassword, BigDecimal balance,Integer tokenType){
        AssetTypeEnum byCode = AssetTypeEnum.getByCode(tokenType);
        if(byCode == null) throw new StructWithCodeException(ResponseCode.ASSET_ERROR);
        vrTokenTradeService.integralAmplifyOf(userId,payPassword,balance, byCode.getCode());
    }

    //资产区块信息
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
