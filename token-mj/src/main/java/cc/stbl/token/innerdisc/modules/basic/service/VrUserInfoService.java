/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogent.cache.redis.annotation.CacheExpiration;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.generator.InviteCodeGenerator;
import cc.stbl.token.innerdisc.common.qrcode.ZXingCodeUtil;
import cc.stbl.token.innerdisc.common.qrcode.ZXingConfig;
import cc.stbl.token.innerdisc.common.remote.RemoteFileTransfer;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.authc.PasswordEncoder;
import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserInfoMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlowDetail;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateFlow;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserDailyIncomeStatistics;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserCard;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInviteCodeImage;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValueRecord;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserRmd;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.modules.sys.service.SysUserRoleService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.admin.user.VrUserProto;
import cc.stbl.token.innerdisc.restapi.app.user.UserProto;

@Service
@CacheConfig(cacheNames = {"vr_user_cache"})
@CacheExpiration(expire = 10 * 60)
public class VrUserInfoService extends DataStoreServiceImpl<String, VrUserInfo, VrUserInfoMapper> {

	
	 private static Logger logger = LoggerFactory.getLogger(VrUserInfoService.class);
	
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EthWalletService walletService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    

    @Autowired
    private VrUserRmdService vrUserRmdService;

    @Autowired
    private SmsVerifyService smsVerifyService;

    @Autowired
    private VrUserInfoService _this;
    
    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private VrUserInviteCodeImageService vrUserInviteCodeImageService;

    @Autowired
    private RemoteFileTransfer remoteFileTransfer;

    @Autowired
    private RebateUserDailyIncomeStatisticsService userDailyIncomeStatisticsService;

    @Autowired
    private VrUserImgService vrUserImgService;
    
    @Autowired
    private SysPropertiesService sysPropertiesService;
    
    @Autowired
    private VrUserLRValueService vrUserLRValueService;
    
	@Autowired
	private ActivateCardFlowService activateCardFlowService;
	@Autowired
    private ActivateCardFlowDetailService activateCardFlowDetailService;
	
	@Autowired
	private VrUserCardService vrUserCardService;
	
	@Autowired
	private ActivateFlowService activateFlowService;
	
	@Autowired 
	private EthWalletService ethWalletService;
	
	@Autowired
	private VrUserLRValueRecordService vrUserLRValueRecordService;

    public static final String RECEIPT_CODE_PATH = "/upload/receipt_code/";
    public static final String USER_ICON_PATH = "/upload/user_icon/";
    private final static String COMMON_PATH = "/upload/ucomm/";

    @Cacheable(key = "'user:vr:userinfo:id_' + #userId")
    public VrUserInfo getUserUseCache(String userId){
        return get(userId);
    }    

    public VrUserInfo getLoginUser() {
        return _this.getUserUseCache(ShiroUtils.getLoginUserId());
    }

    @Transactional
    public VrUserInfo registerUser(VrUserInfo userInfo,String parentUserId,String area){
        String plainPwd = userInfo.getPassword();
        userInfo.setSalt(JavaUUIDGenerator.get());
        userInfo.setPassword(passwordEncoder.encoder(plainPwd,userInfo.getSalt()));
        userInfo.setNewPassword(DESUtil.encryptString(plainPwd));
        userInfo.setPayPassword(DESUtil.encryptString(userInfo.getPayPassword()));
        userInfo.setCreateDate(new Date());
        userInfo.setUpdateDate(new Date());
        userInfo.setStatus(VrUserInfo.USER_STATUS_INACTIVE);
        userInfo.setInviteCode(InviteCodeGenerator.gen());
        String registerUserId = JavaUUIDGenerator.get();
        userInfo.setUserId(registerUserId);
        userInfo.setUserAreaLevel(VrUserInfo.USER_AREA_LEVEL_V1);
       
        String phoneNum = userInfo.getPhoneNum();
        VrUserInfo parentVrUserInfo = null;
        if(!phoneNum.equals(VrUserInfo.TOP_USER)) {
        	parentVrUserInfo = this.get(parentUserId);
//	        String parentUserCode = parentVrUserInfo.getUserCode();
//	        Integer parentUserCodeLevel = parentVrUserInfo.getUserCodeLevel();
//	        Integer userCodeLever = parentUserCodeLevel+1;
//	        String userCode = parentUserCode+"_"+area;
//	        userInfo.setUserCodeLevel(userCodeLever);
//	        userInfo.setUserCode(userCode);
	        
	        //接点人手机号码
	        userInfo.setNodePhoneNum(parentVrUserInfo.getPhoneNum());
	        //推荐人手机号码
	        String userId = ShiroUtils.getLoginUserId();
	        VrUserInfo peakVrUserInfo = this.get(userId);
	        userInfo.setRegisterPhoneNum(peakVrUserInfo.getPhoneNum());
        }else {
        	//顶级用户
        	userInfo.setUserCodeLevel(VrUserInfo.TOP_USER_USER_CODE_LEVEL);
  	        userInfo.setUserCode(VrUserInfo.TOP_USER_USER_CODE);
  	        userInfo.setStatus(VrUserInfo.USER_STATUS_NORMAL);
  	        walletService.createEthWallet(userInfo.getUserId(),userInfo.getPayPassword(),"默认帐号"); 
	    	vrTokenTradeService.activeUser(userInfo.getUserId(), new BigDecimal(0),new BigInteger("6"),"");
        }
        userInfo.setaArea(0L);
        userInfo.setbArea(0L);
        
        VrUserLRValue vrUserLRValueDB = vrUserLRValueService.selectByPidAndArea(parentUserId, area);
        if(vrUserLRValueDB !=null) {
        	throw new StructWithCodeException(ResponseCode.REGISTER_USER_NOT_ALLOW_3);
        }
        
        //添加层级关系表
        
        
        
        VrUserLRValue vrUserLRValue = new VrUserLRValue();
        vrUserLRValue.setUserId(registerUserId);
        vrUserLRValue.setArea(area);
        vrUserLRValue.setPid(parentUserId);
        vrUserLRValue.setStatus(VrUserInfo.USER_STATUS_INACTIVE);
        vrUserLRValue.setCreateDate(new Date());
        vrUserLRValueService.add(vrUserLRValue);
        
        //添加vr用户表
        return super.add(userInfo);
    }
  

    public VrUserInfo createNewUser(VrUserInfo userInfo,String parentInviteCode) throws Exception {
        String plainPwd = userInfo.getPassword();
        userInfo.setSalt(JavaUUIDGenerator.get());
        userInfo.setPassword(passwordEncoder.encoder(plainPwd,userInfo.getSalt()));
        userInfo.setCreateDate(new Date());
        userInfo.setUpdateDate(new Date());
        userInfo.setInviteCode(InviteCodeGenerator.gen());
        userInfo.setUserId(JavaUUIDGenerator.get());
        vrUserRmdService.createVrUserRmd(userInfo,parentInviteCode);
        walletService.createEthWallet(userInfo.getUserId(),plainPwd,"默认帐号"); //api注册不开通钱包
        sysUserRoleService.create(userInfo.getUserId(),"4",userInfo.getUserLevel());
        //生成专属二维码
        vrUserInviteCodeImageService.genInviteCodeImage(userInfo.getUserId(),userInfo.getInviteCode());
        String receiveCode = this.genUserReceiveCode(userInfo.getPhoneNum());
        vrUserImgService.addEthReceiveCode(receiveCode, userInfo.getUserId());
        return super.add(userInfo);
    }

    @Override
    public Pager<VrUserInfo> findPage(VrUserInfo query, Integer pageNo, Integer pageSize) {
        Pager<VrUserInfo> pager = super.findPage(query, pageNo, pageSize);
//        loaderWalletInfo(pager.getList());
        return pager;
    }

    public void loaderWalletInfo(List<VrUserInfo> list) {
        VRToken vrToken = vrTokenManager.getLastVrToken();
        for (VrUserInfo userInfo : list) {
            EthWallet wallet = walletService.getUserWallet(userInfo.getUserId());
            if (wallet != null){
                BigInteger totalBalance = vrTokenManager.totalBalanceOf(wallet.getAddress(),vrToken);
                BigInteger integral = vrTokenManager.integralOf(wallet.getAddress(),vrToken);
                userInfo.setTotalBalance(UnitConvertUtils.toEther(totalBalance).toString());
                userInfo.setIntegral(UnitConvertUtils.toEther(integral).toString());
            }
        }
    }

    public void updatePassword(String userId,String oldPassword,String newPassword){
        VrUserInfo user = get(userId);
        if(!passwordEncoder.encoder(oldPassword,user.getSalt()).equals(user.getPassword())) throw new StructWithCodeException(ResponseCode.LOGIN_PASSWD_ERROR);
        user.setPassword(passwordEncoder.encoder(newPassword,user.getSalt()));
        user.setNewPassword(DESUtil.encryptString(newPassword));
        user.setUpdateDate(new Date());
        update(user);
    }
    
    
    public void updatePasswordBySms(String phoneNum,String password1){
        VrUserInfo user = this.findByPhoneNum(phoneNum);
        user.setPassword(passwordEncoder.encoder(password1,user.getSalt()));
        user.setUpdateDate(new Date());
        user.setNewPassword(DESUtil.encryptString(password1));
        update(user);
    }

    public VrUserInfo getUserByPhoneNum(String phoneNum) {
        VrUserInfo query = new VrUserInfo();
        query.setPhoneNum(phoneNum);
        return findOne(query);
    }

    public VrUserInfo getUserByInviteCode(String inviteCode) {
        VrUserInfo query = new VrUserInfo();
        query.setInviteCode(inviteCode);
        return findOne(query);
    }

    @Transactional
    public void deleteUser(VrUserInfo userInfo) {
    	VrUserInfo v = new VrUserInfo();
    	String userId = userInfo.getUserId();
    	v.setUserId(userId);
    	v.setPhoneNum(userInfo.getPhoneNum()+(new Date().getMonth()+1)+"_"+(new Date().getDate()));
    	v.setSeq(null);
    	v.setStatus(VrUserInfo.USER_STATUS_DELETE);
    	this.update(v);
    	
    	//更新VrUserLRValue表
    	VrUserLRValue vrUserLRValue = new VrUserLRValue();
    	vrUserLRValue.setUserId(userId);
    	vrUserLRValue.setStatus(VrUserInfo.USER_STATUS_DELETE);
    	vrUserLRValueService.update(vrUserLRValue);
        
    }
    
    
    public static void main(String[] args) {
    	List<VrUserInfo> list = getUserCodeList("1_B_A_A_A_B_A_A",300);
    	for(VrUserInfo v:list) {
    		System.out.println(v.getUserCode()+"  "+v.getaArea()+"  "+v.getbArea());
    	}
	}
    
    /**
     * 激活用户
     * @param balance
     * @param phoneNum
     * @return 
     */
    @Transactional
    public void activateUser(String phoneNum,Integer balance){ 
    	String masterUserId = ShiroUtils.getLoginUserId();
		
		//扣除masterUserId的激活卡
		int changeNum = 1;
		long areaValue1 = 300;
		long areaValue2 = 1500;
		long areaValue = 300;
		//选择300扣1张激活卡，选择1500扣5张激活卡
		if(balance == areaValue1) {
			changeNum = 1;
			areaValue = areaValue1;
		}else if(balance == areaValue2) {
			changeNum = 5;
			areaValue = areaValue2;
		}
		
		//查询激活卡是否够扣
		List<VrUserCard> vrUserCardList = vrUserCardService.findByUserIdAndStatus(masterUserId);
		List<String> userIdListTemp = new ArrayList<String>();
	
		if(vrUserCardList == null || vrUserCardList.size() < changeNum) {
			throw new StructWithCodeException(ResponseCode.CARD_NUM_ERROR);
		}
		
		List<String> userIdList = new ArrayList<String>();
		for(VrUserCard v:vrUserCardList) {
			userIdListTemp.add(v.getId());
		}
		//直接拿第一张卡
		if(changeNum == 1) {
			userIdList.add(userIdListTemp.get(0));
		}else {
			//拿5张卡
			userIdList.addAll(userIdListTemp);
		}
		
		//记录激活流水
		ActivateCardFlow activateCardFlow = new ActivateCardFlow();
		Integer changeBefore = vrUserCardService.countByUserIdAndStatus(masterUserId);
		Integer changeAfter = changeBefore-changeNum;
		activateCardFlow.setChangeBefore(changeBefore);
		activateCardFlow.setChangeAfter(changeAfter);
		activateCardFlow.setChangeNum(changeNum);
		//检查用户状态
		VrUserInfo vrUserInfo = this.findByPhoneNum(phoneNum);
		//电话号码不存在
		if(vrUserInfo == null ) {
			throw new StructWithCodeException(ResponseCode.PHONE_NUM_ERROR);
		}
		if(vrUserInfo.getStatus() != VrUserInfo.USER_STATUS_INACTIVE) {
			throw new StructWithCodeException(ResponseCode.ACTIVATE_USER_NOT_ALLOW);
		}
		activateCardFlow.setChangeReason(ActivateCardFlow.ActivateCardFlow_ACTIVATE);
		activateCardFlow.setCreateTime(new Date());
		String uuid = JavaUUIDGenerator.get();
		activateCardFlow.setId(uuid);
		VrUserInfo masterVrUserInfo = this.get(masterUserId);
		activateCardFlow.setPhoneNum(masterVrUserInfo.getPhoneNum());
		activateCardFlow.setRemark("激活会员"+phoneNum+"时扣除的激活卡");
		activateCardFlowService.add(activateCardFlow);
		
		//卡流水详情
		List<ActivateCardFlowDetail> acfdList = new ArrayList<ActivateCardFlowDetail>();
		for(int i=0;i<userIdList.size();i++) {
			VrUserCard vuc = vrUserCardList.get(i);
        	ActivateCardFlowDetail acfd = new ActivateCardFlowDetail();
        	acfd.setActivateCardFlowId(uuid);
        	String cardNo = vuc.getCardNo();
        	logger.info("========cardNo:  "+cardNo);
        	acfd.setCardNo(cardNo);
        	acfd.setId(JavaUUIDGenerator.get());
        	acfdList.add(acfd);
		}
		activateCardFlowDetailService.addBatch(acfdList);
		
		//扣除卡
		vrUserCardService.updateByIds(1,userIdList);
		
		//记录vr_user_lrvalue_record流水表
		String userId = vrUserInfo.getUserId();
		VrUserLRValueRecord vrUserLRValueRecord = new VrUserLRValueRecord();
		vrUserLRValueRecord.setId(JavaUUIDGenerator.get());
		vrUserLRValueRecord.setUserId(userId);
		vrUserLRValueRecord.setStatus(VrUserLRValueRecord.STATUS_0);
		vrUserLRValueRecord.setAddValue(String.valueOf(balance));
		vrUserLRValueRecordService.add(vrUserLRValueRecord);
		
//		MessageProperties messageProperties = new MessageProperties();
//		Message message = new Message(JSONObject.toJSONString(vrUserLRValueRecord).getBytes(), messageProperties);
//		rabbitTemplate.send(rabbitMQ2Properties.getExchange(), rabbitMQ2Properties.getRoutingKey(), message);
		
		//更新  VrUserLRValue表status字段状态
		VrUserLRValue dbVrUserLRValue = vrUserLRValueService.get(vrUserInfo.getUserId());
		VrUserLRValue vrUserLRValue = new VrUserLRValue();
		vrUserLRValue.setUserId(dbVrUserLRValue.getUserId());
		vrUserLRValue.setStatus(VrUserInfo.USER_STATUS_NORMAL);
		vrUserLRValueService.update(vrUserLRValue);
		
		//更新vr_user_info表status字段状态
		vrUserInfo.setStatus(VrUserInfo.USER_STATUS_NORMAL);
		this.update(vrUserInfo);
		
		//记录卡流水
		ActivateFlow activateFlow = new ActivateFlow();
		activateFlow.setCreateTime(new Date());
		activateFlow.setId(JavaUUIDGenerator.get());
		activateFlow.setPhoneNum(phoneNum);
		activateFlow.setUserId(masterUserId);
		activateFlow.setRemark("激活"+areaValue+"$");
		activateFlowService.add(activateFlow);
		
		String payPassword = vrUserInfo.getPayPassword();
		try {
			payPassword = DESUtil.decryptString(payPassword);
		} catch (Exception e) {
		}
		logger.info("vrUserInfo.getPayPassword():{}",payPassword);
		//创建钱包
		walletService.createEthWallet(vrUserInfo.getUserId(),payPassword,"默认帐号"); 
    	
		//调用区块链方法，增加资产
		//资产放大倍数
		String assetMultiple = sysPropertiesService.getString("sys", "asset.multiple");
		vrTokenTradeService.activeUser(vrUserInfo.getUserId(), new BigDecimal(balance.intValue()),new BigInteger(assetMultiple),"");
    }
    
    public void updateABAreaByIds(List userIdList) {
    	mapper.updateABAreaByIds(userIdList);
    }
    
    
    public VrUserInfo selectParentByUserId(String userId) {
    	return mapper.selectParentByUserId(userId);
    }
    
    //更新注册人和下级业绩
    public void updatePerformance(String userId,long areaValue,String registerUserId){
    	String area = vrUserLRValueService.get(userId).getArea();
    	List<VrUserLRValue> allParentList = vrUserLRValueService.getAllParentList(userId,0,1,2);
		List<VrUserInfo> vrUserInfoList = new ArrayList<VrUserInfo>();
		for(int i=0;i<allParentList.size();i++) {
			VrUserLRValue vrUserLRValue = allParentList.get(i);
			String parentUserId = vrUserLRValue.getUserId();
			VrUserInfo vrUserInfo = new VrUserInfo();
			vrUserInfo.setUserId(parentUserId);			
			if(area.equals(VrUserInfo.USER_AAREA)) {
				vrUserInfo.setaArea(areaValue);
				vrUserInfo.setbArea(0L);
			}else {
				vrUserInfo.setaArea(0L);
				vrUserInfo.setbArea(areaValue);
			}
			area = vrUserLRValue.getArea();
			vrUserInfoList.add(vrUserInfo);
			if(parentUserId.equals(registerUserId)) {
				break;
			}
		}
		
		this.updateABAreaByIds(vrUserInfoList);
    }
  
    
    public static List<VrUserInfo> getUserCodeList(String userCode,long areaValue){
    	List<VrUserInfo> userCodeList = new ArrayList<VrUserInfo>();
    	
    	while(userCode.length()>1) {
    		VrUserInfo v = new VrUserInfo();
    		String areaString = userCode.substring(userCode.length()-1, userCode.length());
    		if(areaString.equals(VrUserInfo.USER_AAREA)) {
    			v.setaArea(new Long(areaValue));
    			v.setbArea(0L);
    		}else {
    			v.setaArea(0L);
    			v.setbArea(new Long(areaValue));
    		}
    		userCode = userCode.substring(0, userCode.length()-2);
    		v.setUserCode(userCode);
    		userCodeList.add(v);
    	}
    	
    	return userCodeList;
    }

    @CacheEvict(key = "'user:vr:userinfo:id_' + #userId")
    public void updateUserNickName(String userId, String nickName){
        VrUserInfo user = get(userId);
        user.setUserName(nickName);
        user.setUpdateDate(new Date());
        update(user);
    }

    public void modifyMobileCheck(String mobile, String password){
        VrUserInfo user = this.getUserByPhoneNum(mobile);
        if (user == null){
            throw new StructWithCodeException(ResponseCode.LOGIN_USER_NOT_FOUND_ERROR);
        }
        if(!passwordEncoder.encoder(password,user.getSalt()).equals(user.getPassword())){
            throw new StructWithCodeException(ResponseCode.LOGIN_PASSWD_ERROR);
        }
    }

    public void modifyMobile(String userId, String mobile, String smsCode, String password, Integer codeType){
        smsVerifyService.verifySmsCode(codeType.toString(), mobile, smsCode);
        VrUserInfo userByPhoneNum = this.getUserByPhoneNum(mobile);
        if (userByPhoneNum != null){
            throw new StructWithCodeException(ResponseCode.MOBILE_DUPLICATE_ERROR,mobile);
        }
        VrUserInfo vrUserInfo = get(userId);
        vrUserInfo.setPhoneNum(mobile);
        vrUserInfo.setPassword(passwordEncoder.encoder(password,vrUserInfo.getSalt()));
        super.update(vrUserInfo);
    }
    
    public String getWalletAddress() {
    	String userId = ShiroUtils.getLoginUserId();
    	EthWallet ethWallet = ethWalletService.getUserWallet(userId);
    	String address = "";
    	if(ethWallet!=null) {
    		address = ethWallet.getAddress();
    	}
    	return address;
    }

    @Transactional
    public void updatePayPassword(VrUserInfo vrUserInfo,String newPayPassword){
    	String payPassword = vrUserInfo.getPayPassword();
    	try {
			payPassword = DESUtil.decryptString(payPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	String userId = vrUserInfo.getUserId();
    	VrUserInfo newVrUserInfo = new VrUserInfo();
    	newVrUserInfo.setUserId(userId);
    	newVrUserInfo.setPayPassword(DESUtil.encryptString(newPayPassword));
    	 super.update(newVrUserInfo);
    	 walletService.updatePasswordByUserId(userId, payPassword, newPayPassword);
    }
    @CacheEvict(key = "'user:vr:userinfo:id_' + #userId")
    public void saveUserIcon(String path, String userId){
        logger.info("上传用户头像相对路径:" + path);
        VrUserInfo vrUserInfo = super.get(userId);
        if (!path.startsWith("/")){
            path = "/" + path;
        }
        vrUserInfo.setUserIcon(path);
        super.update(vrUserInfo);
    }

    public void retrievePassword(String userId, String mobile, String smsCode, String password, Integer codeType){
        smsVerifyService.verifySmsCode(codeType.toString(), mobile, smsCode);
        VrUserInfo user = get(userId);
        user.setPassword(passwordEncoder.encoder(password,user.getSalt()));
        user.setUpdateDate(new Date());
        update(user);
    }

    public UserProto.ResponseMyPromotion getMyPromotionInfo(String userId, UserProto.RequestMyPromotion requestMyPromotion){
        VrUserInfo vrUserInfo = super.get(userId);
        if (vrUserInfo == null){
            throw new StructWithCodeException(ResponseCode.LOGIN_USER_NOT_FOUND_ERROR);
        }
        VrUserInviteCodeImage codeImage = vrUserInviteCodeImageService.getByUserId(userId);
        if (codeImage == null){
            throw new StructWithCodeException(ResponseCode.INVITE_CODE_NOT_FOUND_ERROR);
        }
        UserProto.ResponseMyPromotion myPromotion = new UserProto.ResponseMyPromotion();
        List<UserProto.ResponseSubInfo> lowerInfoList = new ArrayList<>();
        myPromotion.setInviteCode(codeImage.getInviteCode());
        myPromotion.setUserId(userId);
        myPromotion.setInviteCodeImg(codeImage.getQrCodeImg());
        VrUserRmd vrUserRmd = new VrUserRmd();
        vrUserRmd.setParentUserId(userId);
        if (requestMyPromotion.getDate() != null){
            vrUserRmd.setStartRegisterDate(requestMyPromotion.getDate());
            vrUserRmd.setEndRegisterDate(DateUtils.addDays(requestMyPromotion.getDate(),1));
        }
        List<VrUserRmd> list = vrUserRmdService.findPageList(vrUserRmd, requestMyPromotion.getPageNo(), requestMyPromotion.getPageSize());
        if (list != null && list.size() > 0){
            for (VrUserRmd userRmd : list){
                VrUserInfo subUser = super.get(userRmd.getUserId());
                UserProto.ResponseSubInfo subInfo = new UserProto.ResponseSubInfo();
                subInfo.setUserId(subUser.getUserId());
                subInfo.setMobile(subUser.getPhoneNum());
                subInfo.setNickName(subUser.getUserName());
                RebateUserDailyIncomeStatistics userIncome = userDailyIncomeStatisticsService.getUserIncome(subUser.getUserId());
                subInfo.setContributedAssets(userIncome == null ? new BigDecimal("0") : userIncome.getIncome());
                lowerInfoList.add(subInfo);
            }
        }
        myPromotion.setLowerInfoList(lowerInfoList);

        return myPromotion;
    }

    public String genUserReceiveCode(String content){
        try {
            ZXingConfig zXingConfig = new ZXingConfig();
            int qRCode = new Color(0,0,0,1).getRGB();//二维码颜色
            zXingConfig.setQRCode(qRCode);
            zXingConfig.setContent(content);
            ZXingCodeUtil zXingCodeUtil = new ZXingCodeUtil();
            BufferedImage qr_codeBufferedImage = zXingCodeUtil.getQR_CODEBufferedImage(zXingConfig);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qr_codeBufferedImage, "jpg", byteArrayOutputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            String fileName = UUID.randomUUID().toString() + ".jpg";
            // 上传
            String path = COMMON_PATH + DateFormatUtils.format(new Date(),"yyyyMMdd") + "/" + ShiroUtils.getLoginUserId();
            String name = JavaUUIDGenerator.get() + trySubFileName(fileName);;
            String filePath = remoteFileTransfer.upload(path,name,inputStream);
            return filePath;
        }catch (Exception e){
            throw new StructWithCodeException(ResponseCode.GEN_QRCODE_ERROR);
        }
    }

    private String trySubFileName(String fileName) {
        if(!fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }

    public List<VrUserInfo> findAllList(){
        return mapper.findAllList();
    }
    
    public VrUserInfo findByPhoneNum(String phoneNum) {
    	return mapper.findByPhoneNum(phoneNum);
    }
    
    public VrUserInfo findByUserCodeAndLevel(String userCode,int userCodeLevel) {
    	return mapper.findByUserCodeAndLevel(userCode,userCodeLevel);
    }
    
    
    /**
     * 获取3层用户列表
     * @param phoneNum
     * @return
     */
    public VrUserInfo findDownLevelUsresByPhoneNum(String phoneNum) {
    	
    	VrUserInfo  vrUserInfo = mapper.findByPhoneNum(phoneNum);
    	String userId = vrUserInfo.getUserId();
    	findDownLevelUsresByPhoneNumDetail(userId,vrUserInfo,0); 
    	return vrUserInfo;
    }
    
    
    public void findDownLevelUsresByPhoneNumDetail(String userId,VrUserInfo node,int flag) {
    	List<VrUserInfo> vrUserLRValueList = mapper.selectSubUser(userId);
    	if(vrUserLRValueList==null)return;
    	for(int i=0;i<vrUserLRValueList.size();i++) {
    		VrUserInfo dbVrUserInfo = vrUserLRValueList.get(i);
    		String userIdNew = dbVrUserInfo.getUserId();
    		String area = dbVrUserInfo.getArea();
    		if(area.equals(VrUserInfo.USER_AAREA)) {
    			node.setLeftNode(dbVrUserInfo);
    		}else {
    			node.setRightNode(dbVrUserInfo);
    		}
    		if(flag == 0) {
				findDownLevelUsresByPhoneNumDetail(userIdNew,dbVrUserInfo,1);
			}
    	}
    }
    
	/**
	 * 查询需要统计mj奖励的用户列表
	 * 
	 * @param minLevel
	 *            最小统计层级，如不统计一级，则传入 user_code_level=2
	 */
	public List<VrUserInfo> getPrizeUserList(int minLevel, String createDate) {
		return mapper.getPrizeUserList(minLevel, createDate);
	}

	/**
	 * 查询指定用户的下级用户
	 * 
	 * @param userCode
	 * @param currentLevel
	 *            当前用户对应的级别
	 * @param maxLevel
	 *            最大统计的层级数
	 */
	public List<VrUserInfo> getSubLevelUserList(String userCode, int currentLevel, int maxLevel) {
		return mapper.getSubLevelUserList(userCode, currentLevel + maxLevel);
	}

    /**
     * 根据姓名模糊查询分页数据.用户为正常用户 status = 1
     * @param userName 姓名。可以为空，或者进行模糊搜索
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<VrUserInfo> findUserListByName(String userName, Integer pageNo, Integer pageSize) {
        Long total = this.mapper.findCountByName(userName);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<VrUserInfo>(), total);
        }
        List<VrUserInfo> list = this.mapper.findListByName(userName,new RowBounds(offset,pageSize));
        return new Pager<>(pageNo, pageSize, list, total);
    }
    
    @Transactional
    public void saveUserInfoAttribute(VrUserInfo vrUserInfo,VrUserProto.UserAttribute request) {
    	 String payPassword = request.getPayPassword();
    	 String userId = vrUserInfo.getUserId();
         logger.info("payPassword======"+payPassword);
         logger.info("request.getNewPassword()======"+request.getNewPassword());
         String newPassword = request.getNewPassword();
         vrUserInfo.setUserName(request.getUserName());
         vrUserInfo.setPassword(passwordEncoder.encoder(newPassword,vrUserInfo.getSalt()));
         vrUserInfo.setNewPassword(DESUtil.encryptString(newPassword));
         String oldPayPassword = vrUserInfo.getPayPassword();
         try {
			oldPayPassword = DESUtil.decryptString(oldPayPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
         vrUserInfo.setPayPassword(DESUtil.encryptString(payPassword));
         this.update(vrUserInfo);
         walletService.updatePasswordByUserId(userId, oldPayPassword, payPassword);
    }

    /**
     * 根据手机号查询会员账户资产信息.用户为正常用户 status = 1
     * @param query 查询条件
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager<VrUserInfo> findUserAccountByPhone(VrUserInfo query, Integer pageNo, Integer pageSize) {
        query.setStatus(1); //查找正常用户的数据
        Long total = this.mapper.findCount(query);
        Integer offset = (pageNo - 1) * pageSize;
        if(total == 0) {
            return new Pager<>(pageNo, pageSize, new ArrayList<VrUserInfo>(), total);
        }
        List<VrUserInfo> list = this.mapper.findList(query,new RowBounds(offset,pageSize));

        //请实现下面的内容
        for(VrUserInfo userInfo : list) {
            userInfo.getUserId();
            userInfo.setPassword("");   //清空密码
            userInfo.setSalt("");       //清空
            userInfo.setIdCard("");     //清空
            userInfo.setInviteCode(""); //清空
            userInfo.setPassword("");   //清空


            userInfo.setActiveCode(""); //设置激活码账户
            userInfo.setPropertyAccount(""); //设置资产
            userInfo.setMjAccount("");  //设置MJ资产
            userInfo.setAiicAccount("");    //设置AIIC资产
        }

        return new Pager<>(pageNo, pageSize, list, total);
    }
}