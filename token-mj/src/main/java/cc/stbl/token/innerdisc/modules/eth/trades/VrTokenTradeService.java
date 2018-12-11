package cc.stbl.token.innerdisc.modules.eth.trades;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.TradeNoGenerator;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.eth.wallet.EthWalletUtils;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserRmdService;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;
import cc.stbl.token.innerdisc.modules.eth.entity.EthTradeRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthIntegralAmplifyService;
import cc.stbl.token.innerdisc.modules.eth.service.EthReturnedIntegralService;
import cc.stbl.token.innerdisc.modules.eth.service.EthTradeRecordService;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.accelerate.AbstractIntegralAdapter;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.trades.CoinSettingProto;
import com.alibaba.fastjson.JSONObject;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VrTokenTradeService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private EthWalletService walletService;

    @Autowired
    private EthTradeRecordService tradeRecordService;

    @Autowired
    private EthIntegralAmplifyService amplifyService;

    @Autowired
    private EthReturnedIntegralService returnedIntegralService;

    @Autowired
    private Credentials sysCredentials;

    @Autowired
    private TradeNoGenerator tradeNoGenerator;

    @Autowired
    private VrUserRmdService vrUserRmdService;

    @Autowired
    private AbstractIntegralAdapter integralAdapter;

    //重置资产
    public boolean restAsset(String resetUserId,BigDecimal resetAmount,String tradeNo){
        EthWallet wallet = walletService.getUserWallet(resetUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,resetUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.resetAsset(wallet.getAddress(),
                vrTokenManager.balanceOf(wallet.getAddress(),vrToken),
                vrTokenManager.limitBalanceOf(wallet.getAddress(),vrToken),
                UnitConvertUtils.toWei(resetAmount),
                tradeNo).sendAsync();
        return true;
    }
    //重置所有
    public boolean restAll(String resetUserId,BigDecimal resetAiicAmount,BigDecimal resetMjAmount,BigDecimal resetAssetAmount,String tradeNo){
        EthWallet wallet = walletService.getUserWallet(resetUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,resetUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.resetAsset(wallet.getAddress(),
                UnitConvertUtils.toWei(resetAiicAmount),
                UnitConvertUtils.toWei(resetMjAmount),
                UnitConvertUtils.toWei(resetAssetAmount),
                tradeNo).sendAsync();
        return true;
    }
    //重置Aiic
    public boolean restAiic(String resetUserId,BigDecimal resetAmount,String tradeNo){
        EthWallet wallet = walletService.getUserWallet(resetUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,resetUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.resetAsset(wallet.getAddress(),
                UnitConvertUtils.toWei(resetAmount),
                vrTokenManager.limitBalanceOf(wallet.getAddress(),vrToken),
                vrTokenManager.integralOf(wallet.getAddress(),vrToken),tradeNo).sendAsync();
        return true;
    }
    //重置mj
    public boolean restMj(String resetUserId,BigDecimal resetAmount,String tradeNo){
        EthWallet wallet = walletService.getUserWallet(resetUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,resetUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.resetAsset(wallet.getAddress(),
                vrTokenManager.balanceOf(wallet.getAddress()),
                UnitConvertUtils.toWei(resetAmount),
                vrTokenManager.integralOf(wallet.getAddress())
                ,tradeNo).sendAsync(); //  AIIC
        return true;
    }

    //平台充值 AIIC
    public boolean chargeAsset(String toUserId, BEnum typeEnum, BigDecimal chargeAmount, String remark){
        EthWallet wallet = walletService.getUserWallet(toUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromAddress(sysCredentials.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(typeEnum.type);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setTradeAmount(chargeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNoGenerator.get());
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.transferExt(wallet.getAddress(),UnitConvertUtils.toWei(chargeAmount),tradeId).sendAsync(); //  AIIC
         return true;
    }

    //平台充值 MJ
    public boolean chargeAssetMj(String toUserId, BEnum typeEnum, BigDecimal chargeAmount, String remark){
        EthWallet wallet = walletService.getUserWallet(toUserId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setToRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromAddress(sysCredentials.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(typeEnum.type);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setTradeAmount(chargeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNoGenerator.get());
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.limitedTransfer(wallet.getAddress(),UnitConvertUtils.toWei(chargeAmount),tradeId).sendAsync(); //充值 MJ
        return true;
    }

    //下线充值
    public boolean chargeSubAsset(String toUserId, BigDecimal chargeAmount,String payPwd,String remark){
        String fromUserId=ShiroUtils.getLoginUserId();
        if(!vrUserRmdService.checkMembership(fromUserId,toUserId)){
            return false;
        }
        this.transferFromExt(payPwd,fromUserId,toUserId,chargeAmount,"给下线充值",remark);
        return true;
    }

    //转账 aiic - aiic
    public void transferFromExt(String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark){
        transferFromExt(BEnum.TRANSFER,payPassword,fromUserId,toUserId,tradeAmount,fromRemark,toRemark,tradeNoGenerator.get());
    }
    //转账 aiic - aiic
    public void transferFromExt(BEnum bEnum,String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthWallet fromWallet = walletService.getUserWallet(fromUserId);
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"收款");
        if(fromWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"付款");
        Credentials fromCredentials;
        try {
            fromCredentials = EthWalletUtils.loadCredentials(payPassword,fromWallet.getWalletFile());
        } catch (Exception e) {
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fromWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken sysVrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.balanceOf(fromCredentials.getAddress(),sysVrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        sysVrToken.transferFromExt(fromWallet.getAddress(),toWallet.getAddress(),tradeAmountBigInteger,tradeId).sendAsync();
    }

    //转账  mj - mj
    public void transferMJFromExt(String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark){
    	transferMJFromExt(BEnum.TRANSFER,payPassword,fromUserId,toUserId,tradeAmount,fromRemark,toRemark,tradeNoGenerator.get());
    }
    
    //转账 mj - mj
    public void transferMJFromExt(BEnum bEnum,String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthWallet fromWallet = walletService.getUserWallet(fromUserId);
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"收款");
        if(fromWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"付款");
        Credentials fromCredentials;
        try {
            fromCredentials = EthWalletUtils.loadCredentials(payPassword,fromWallet.getWalletFile());
        } catch (Exception e) {
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fromWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken sysVrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.limitBalanceOf(fromWallet.getAddress(),sysVrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        sysVrToken.limitedTransferFrom(fromWallet.getAddress(),toWallet.getAddress(),tradeAmountBigInteger,tradeId).sendAsync(); // mj - mj
    }


    //从发起人的 mj 转入到收款人的 冻结账户
    public void transferMJToLock(BEnum bEnum,String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthWallet fromWallet = walletService.getUserWallet(fromUserId);
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"收款");
        if(fromWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,"付款");
        Credentials fromCredentials;
        try {
            fromCredentials = EthWalletUtils.loadCredentials(payPassword,fromWallet.getWalletFile());
        } catch (Exception e) {
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fromWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken sysVrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.limitBalanceOf(fromCredentials.getAddress(),sysVrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        sysVrToken.limitedTransferLock(fromWallet.getAddress(),toWallet.getAddress(),tradeAmountBigInteger,tradeId).sendAsync();//  mj -> 冻结
    }

    //冻结用户AIIC金额 ,//挂单平台,冻结用户金额 BusinessTypeEnum.LINKED_LOCK
    public void lockBalance(String userId, BEnum typeEnum, BigDecimal lockAmount,String remark){
        lockBalance(userId,typeEnum,lockAmount,remark,tradeNoGenerator.get());
    }
    //冻结用户AIIC金额 ,//挂单平台,冻结用户金额 BusinessTypeEnum.LINKED_LOCK
    public void lockBalance(String userId, BEnum typeEnum, BigDecimal lockAmount,String remark,String tradeNo){
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(lockAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.balanceOf(wallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.LINKED_TRADE_BALANCE_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setFromRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(typeEnum.type);
        tradeRecord.setTradeAmount(lockAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setToUserId(userId);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecordService.add(tradeRecord);
        vrToken.lockBalanceFrom(wallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }

    //冻结用户MJ金额 ,//挂单平台,冻结用户金额 BusinessTypeEnum.LINKED_LOCK
    public void lockMJBalance(String userId, BEnum typeEnum, BigDecimal lockAmount,String remark,String tradeNo){
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(lockAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.balanceOf(wallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setFromRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(typeEnum.type);
        tradeRecord.setTradeAmount(lockAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setToUserId(userId);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecordService.add(tradeRecord);
        vrToken.lockLimitBalanceFrom(wallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }

    public void unLockBalance(String userId,BEnum btype,BigDecimal unLockAmount,String remark){
        unLockBalance(userId,btype,unLockAmount,remark,tradeNoGenerator.get());
    }
    //用户金额 lock - aiic
    public void unLockBalance(String userId,BEnum btype,BigDecimal unLockAmount,String remark,String tradeNo){
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(unLockAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.lockedBalanceOf(wallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setFromRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(btype.type);
        tradeRecord.setTradeAmount(unLockAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setToUserId(userId);
        tradeRecordService.add(tradeRecord);
        vrToken.unLockBalance(wallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }

    // lock - mj
    public void unLockMjBalance(String userId,BEnum btype,BigDecimal unLockAmount,String remark,String tradeNo){
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(unLockAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.lockedBalanceOf(wallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setFromRemark(remark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(btype.type);
        tradeRecord.setTradeAmount(unLockAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecord.setToAddress(wallet.getAddress());
        tradeRecord.setToUserId(userId);
        tradeRecordService.add(tradeRecord);
        vrToken.unLockBalanceToLimit(wallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }

    //用户资产返币 mj and aiic
    public BigDecimal integralRebateByAmplify(String userId){
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.NO_WALLET);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger bigInteger = vrTokenManager.integralOf(wallet.getAddress(),vrToken); //  用户总资产
        BigDecimal totalAmount = UnitConvertUtils.toEther(bigInteger);
        totalAmount = totalAmount.setScale(18,BigDecimal.ROUND_HALF_UP);
        BigDecimal returnIntegralETH = totalAmount.multiply(integralAdapter.readRebateRatio(userId)); // 根据加上比例获得当前释放的
        EthReturnedIntegral ri = new EthReturnedIntegral();
        ri.setAddress(wallet.getAddress());
        ri.setUserId(wallet.getUserId());
        ri.setId(JavaUUIDGenerator.get());
        ri.setIntegral(returnIntegralETH);
        ri.setStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        ri.setOptDate(new Date());
        ri.setContractAt(vrToken.getContractAddress());
        returnedIntegralService.add(ri);
        vrToken.integralRebate(wallet.getAddress(),
                UnitConvertUtils.toWei(returnIntegralETH),
                integralAdapter.limitedRatio(userId)
                ,ri.getId()).sendAsync();
        return returnIntegralETH;
    }

    //用户资产返币 mj and aiic
    public void integralReturn(String userId,BigDecimal aiic,BigDecimal mj,BigDecimal integral,String tradeId){
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.NO_WALLET);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger bigInteger = vrTokenManager.integralOf(wallet.getAddress(),vrToken); //  用户总资产
        BigDecimal totalAmount = UnitConvertUtils.toEther(bigInteger);
        if(totalAmount.doubleValue() < integral.doubleValue()) throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        EthReturnedIntegral ri = new EthReturnedIntegral();
        ri.setAddress(wallet.getAddress());
        ri.setUserId(wallet.getUserId());
        ri.setId(tradeId);
        ri.setIntegral(integral);
        ri.setBalance(aiic);
        ri.setLimitedBalance(mj);
        ri.setStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        ri.setOptDate(new Date());
        ri.setContractAt(vrToken.getContractAddress());
        returnedIntegralService.add(ri);
        vrToken.integralReturn(wallet.getAddress(),UnitConvertUtils.toWei(integral),UnitConvertUtils.toWei(aiic),UnitConvertUtils.toWei(mj),ri.getId()).sendAsync();
    }

    /**
     * @param userId 用户id
     * @param balance 激活金额
     * @param magnification 激活倍数
     * @param extArgs 扩展参数 例如 {"激活卡":"","用户":""} ....随意发挥
     * @return
     */
    public boolean activeUser(String userId,BigDecimal balance,BigInteger magnification,String extArgs){
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        EthIntegralAmplify amplify = new EthIntegralAmplify();
        amplify.setUserId(userId);
        amplify.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        amplify.setId(tradeNoGenerator.get());
        amplify.setAddress(wallet.getAddress());
        amplify.setTotalToken(balance);
        amplify.setCreateDate(new Date());
        amplify.setStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        amplify.setTokenType(TradeConsts.FLOW_TYPE_INTEGRAL);
        amplify.setOptType(TradeConsts.OPT_TYPE_BY_SYSTEM_USER);
        amplify.setExtArgs(extArgs);
//        createTradeRecordByAmplify(amplify,BEnum.AMPLIFY);
        amplifyService.add(amplify);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(balance);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.balanceOf(sysCredentials.getAddress())) > 0)throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        vrToken.activeUser(amplify.getAddress(), tradeAmountBigInteger,magnification,amplify.getId()).sendAsync();
        return true;
    }

    /**
     * mj 或 aiic 兑换成资产
     */
    public boolean integralAmplifyOf(String userId,String payPassword,BigDecimal balance,Integer tokenType){
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        try {
             EthWalletUtils.loadCredentials(payPassword,wallet.getWalletFile());
        } catch (Exception e) {
            logger.error(e.getMessage() + "==> " + payPassword,e);
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        EthIntegralAmplify amplify = new EthIntegralAmplify();
        amplify.setUserId(userId);
        amplify.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        amplify.setId(tradeNoGenerator.get());
        amplify.setAddress(wallet.getAddress());
        amplify.setTotalToken(balance);
        amplify.setCreateDate(new Date());
        amplify.setStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        amplify.setTokenType(tokenType);
        amplify.setOptType(TradeConsts.OPT_TYPE_BY_USER);
//        createTradeRecordByAmplify(amplify,BEnum.AMPLIFY);
        amplifyService.add(amplify);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(balance);
        if(tokenType == TradeConsts.FLOW_TYPE_BALANCE) {
            if(tradeAmountBigInteger.compareTo(vrTokenManager.balanceOf(wallet.getAddress())) > 0)throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
            vrToken.integralAmplifyOf(amplify.getAddress(), tradeAmountBigInteger,integralAdapter.readMagnification(userId),amplify.getId()).sendAsync();
        }
        if(tokenType == TradeConsts.FLOW_TYPE_LIMITED_BALANCE) {
            if(tradeAmountBigInteger.compareTo(vrTokenManager.limitBalanceOf(wallet.getAddress())) > 0)throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
            vrToken.integralLimitedAmplifyOf(amplify.getAddress(), tradeAmountBigInteger,integralAdapter.readMagnification(userId),amplify.getId()).sendAsync();
        }

        return true;
    }


    //扣除用户Aiic
    public boolean deductAsset(String userId, BEnum businessTypeEnum, BigDecimal amount, String toRemark){
       return deductAsset(userId,businessTypeEnum,amount,toRemark,tradeNoGenerator.get());
    }
    //扣除用户Aiic
    public boolean deductAsset(String userId, BEnum businessTypeEnum, BigDecimal amount, String toRemark,String tradeNo) {
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setToAddress(sysCredentials.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(businessTypeEnum.type);
        tradeRecord.setTradeAmount(amount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.transferFromExt(wallet.getAddress(),sysCredentials.getAddress(),UnitConvertUtils.toWei(amount),tradeId);
        return true;
    }

    
    //扣除用户mj
    public boolean deductMj(String userId, BEnum businessTypeEnum, BigDecimal amount, String toRemark) {
    	return deductMj(userId,  businessTypeEnum,  amount,  toRemark, tradeNoGenerator.get());
    }
    
    //扣除用户mj
    public boolean deductMj(String userId, BEnum businessTypeEnum, BigDecimal amount, String toRemark,String tradeNo) {
        EthWallet wallet = walletService.getUserWallet(userId);
        if(wallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,userId);
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setFromAddress(wallet.getAddress());
        tradeRecord.setFromUserId(userId);
        tradeRecord.setToAddress(sysCredentials.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(businessTypeEnum.type);
        tradeRecord.setTradeAmount(amount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        vrToken.limitedTransferFrom(wallet.getAddress(),sysCredentials.getAddress(),UnitConvertUtils.toWei(amount),tradeId);
        return true;
    }

    //转账
    public void lockedTransferFrom(String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark){
        lockedTransferFrom(BEnum.LOCK_TRANSFER,fromUserId,toUserId,tradeAmount,fromRemark,toRemark,tradeNoGenerator.get());
    }
    //冻结资产 - 转账
    public void lockedTransferFrom(BEnum bEnum,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthWallet fromWallet = walletService.getUserWallet(fromUserId);
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        if(fromWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,fromUserId);
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fromWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.lockedBalanceOf(fromWallet.getAddress(),vrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        vrToken.lockedTransferFrom(fromWallet.getAddress(),toWallet.getAddress() ,tradeAmountBigInteger,tradeId).sendAsync();
    }

    //平台转账,从平台的现金账户扣除金额，转入接收人的冻结资产
    public void tradePlatformTransfer(BEnum bEnum,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthTradeRecord tradeRecord = new EthTradeRecord();
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
//        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(sysCredentials.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.balanceOf(sysCredentials.getAddress(),vrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        vrToken.lockBalanceFromExt(sysCredentials.getAddress(),toWallet.getAddress(), tradeAmountBigInteger,tradeId).sendAsync();
    }

    //从发起人的现金账户扣除金额，转入接收人的冻结资产
    public void tradePlatformTransfer(BEnum bEnum,String payPassword,String fromUserId,String toUserId,BigDecimal tradeAmount,String fromRemark,String toRemark,String tradeNo){
        EthWallet fromWallet = walletService.getUserWallet(fromUserId);
        EthWallet toWallet = walletService.getUserWallet(toUserId);
        if(toWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        if(fromWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,fromUserId);
        try {
            Credentials fromCredentials = EthWalletUtils.loadCredentials(payPassword,fromWallet.getWalletFile());
        } catch (Exception e) {
            throw new StructWithCodeException(ResponseCode.LOGIN_PAY_PWD_ERROR);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fromWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(toWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);
        String tradeId = tradeRecord.getId();
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger balanceOf = vrTokenManager.balanceOf(fromWallet.getAddress(),vrToken);
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        vrToken.lockBalanceFromExt(fromWallet.getAddress(),toWallet.getAddress(), tradeAmountBigInteger,tradeId).sendAsync();

    }

    //平台转账，从发起人的冻结账户扣除金额，转入接收人的现金资产
    public void tradePlatformTransferRefund(String fromUserId,String toUserId,BigDecimal tradeAmount,BEnum bEnum,String fromRemark,String toRemark,String tradeNo){
        EthWallet fWallet = walletService.getUserWallet(fromUserId);
        EthWallet tWallet = walletService.getUserWallet(toUserId);
        if(fWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        if(tWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,fromUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.lockedBalanceOf(fWallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(tWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);

        BigInteger balanceOf = vrTokenManager.lockedBalanceOf(fWallet.getAddress(),vrToken);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        vrToken.tradePlatformTransfer(fWallet.getAddress(),tWallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }

    //从发起人的冻结账户扣除金额，转入接收人的Mj账户
    public void tradePlatformTransferLimit(String fromUserId,String toUserId,BigDecimal tradeAmount,BEnum bEnum,String fromRemark,String toRemark,String tradeNo){
        EthWallet fWallet = walletService.getUserWallet(fromUserId);
        EthWallet tWallet = walletService.getUserWallet(toUserId);
        if(fWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,toUserId);
        if(tWallet == null) throw new StructWithCodeException(ResponseCode.WALLET_NOT_EXISTS,fromUserId);
        VRToken vrToken = vrTokenManager.getLastVrToken();
        BigInteger tradeAmountBigInteger = UnitConvertUtils.toWei(tradeAmount);
        if(tradeAmountBigInteger.compareTo(vrTokenManager.lockedBalanceOf(fWallet.getAddress())) > 0)  {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        EthTradeRecord tradeRecord = new EthTradeRecord();
        tradeRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        tradeRecord.setFromFlowType(TradeConsts.FLOW_TYPE_LOCKED_BALANCE);
        tradeRecord.setToFlowType(TradeConsts.FLOW_TYPE_LIMITED_BALANCE);
        tradeRecord.setFromRemark(fromRemark);
        tradeRecord.setToRemark(toRemark);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setFromUserId(fromUserId);
        tradeRecord.setFromAddress(fWallet.getAddress());
        tradeRecord.setToUserId(toUserId);
        tradeRecord.setToAddress(tWallet.getAddress());
        tradeRecord.setId(JavaUUIDGenerator.get());
        tradeRecord.setTradeType(bEnum.type);
        tradeRecord.setTradeAmount(tradeAmount);
        tradeRecord.setTradeStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        tradeRecord.setTradeNo(tradeNo);
        tradeRecordService.add(tradeRecord);

        BigInteger balanceOf = vrTokenManager.lockedBalanceOf(fWallet.getAddress(),vrToken);
        if(balanceOf.doubleValue() < tradeAmountBigInteger.doubleValue()) {
            throw new StructWithCodeException(ResponseCode.TRADE_USER_ASSET_LESS);
        }
        vrToken.tradePlatformTransferLimit(fWallet.getAddress(),tWallet.getAddress(),tradeAmountBigInteger,tradeRecord.getId()).sendAsync();
    }
    public CoinSettingProto.ImportDataReport importDataFrom(String address,String batch){return importDataFrom(address,true,batch);}
    public CoinSettingProto.ImportDataReport importDataFrom(String address,Boolean mock,String batch) {
        VRToken old = vrTokenManager.loadAdminVrToken(address);
        VRToken newV = vrTokenManager.getLastVrToken();
        CoinSettingProto.ImportDataReport report = new CoinSettingProto.ImportDataReport();
        List<EthWallet> ethWallets = walletService.findList(new EthWallet());
        report.setTotalSize(ethWallets.size());
        for (EthWallet ethWallet : ethWallets) {
            BigInteger aiic = vrTokenManager.balanceOf(ethWallet.getAddress(),old);
            BigInteger mj = vrTokenManager.limitBalanceOf(ethWallet.getAddress(),old);
            BigInteger integral = vrTokenManager.integralOf(ethWallet.getAddress(),old);
            JSONObject json = new JSONObject();
            json.put("userId",ethWallet.getUserId());
            json.put("address",ethWallet.getAddress());
            json.put("aiic",UnitConvertUtils.toEther(aiic));
            json.put("mj",UnitConvertUtils.toEther(mj));
            json.put("integral",UnitConvertUtils.toEther(integral));

            if(
              BigInteger.ZERO.compareTo(aiic) == 0 &&
              BigInteger.ZERO.compareTo(mj) == 0 &&
              BigInteger.ZERO.compareTo(integral) == 0
            ) {
                logger.info("batch={},userId={} 所有资产为 0,不需要导入",batch,ethWallet.getUserId());
                continue;
            }
            report.setImportSize(report.getImportSize()+1);
            if(!mock) try {
                newV.resetAsset(ethWallet.getAddress(),aiic,mj,integral,batch + ":" + ethWallet.getUserId()).sendAsync();
//                Thread.sleep(1000);
                logger.info("batch={},object={} 所有资产为 0,不需要导入",json.toJSONString());
            } catch (Exception e) {
                json.put("error",e.getMessage());
            }
            report.getMessages().add(json);
            report.setSuccessSize(report.getSuccessSize() +1);
        }
        return report;
    }
}
