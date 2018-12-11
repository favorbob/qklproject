package cc.stbl.token.innerdisc.restapi.app.mastersubaccount;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.sms.SmsVerifyService;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.MasterSubAccount;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.MasterSubAccountService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/masterSubAccount")
@Api(description = "主子账号模块")
public class MasterSubAccountController {

    public static Logger logger = LoggerFactory.getLogger(MasterSubAccountController.class);

    @Autowired
    private MasterSubAccountService masterSubAccountService;
    @Autowired
    private VrUserInfoService vrUserInfoService;
    @Autowired
    private SmsVerifyService smsVerifyService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation("获取主子账号列表")
    public Response getMasterSubAccounts(){
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo vrUserInfo = vrUserInfoService.get(userId);
    	MasterSubAccount m = new MasterSubAccount();
    	String phoneNum = vrUserInfo.getPhoneNum();
    	m.setMasterPhoneNum(phoneNum);
    	List<MasterSubAccount> masterSubAccountList = new ArrayList<MasterSubAccount>();
    	List<MasterSubAccount> tempList = masterSubAccountService.findList(m);
    	if(tempList != null && tempList.size()>0) {
    	}else {
    		tempList = masterSubAccountService.findListBySubPhoneNum(phoneNum);
    		if(tempList!=null &tempList.size()>0){
	    		MasterSubAccount tempMasterSubAccount = tempList.get(0);
	    		MasterSubAccount  masterSubAccount = new MasterSubAccount();
	    		String masterPhoneNum = tempMasterSubAccount.getMasterPhoneNum();
	    		VrUserInfo masterVrUserInfo = vrUserInfoService.findByPhoneNum(masterPhoneNum);
	        	masterSubAccount.setMasterPhoneNum(masterPhoneNum);
	        	String newPassword;
				try {
					newPassword = DESUtil.decryptString(masterVrUserInfo.getNewPassword());
					masterSubAccount.setNewPassword(newPassword);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	masterSubAccountList.add(masterSubAccount);
    		}
    	}
    	for(MasterSubAccount msa:tempList) {
    		String subPhoneNum = msa.getSubPhoneNum();
    		VrUserInfo subVrUerInfo = vrUserInfoService.findByPhoneNum(subPhoneNum);
    		String tempPassword = subVrUerInfo.getNewPassword();
    		try {
    			tempPassword = DESUtil.decryptString(tempPassword);
    			msa.setNewPassword(tempPassword);
    			msa.setMasterPhoneNum("");
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	if(masterSubAccountList.size()==0) {
    		String masterPassword = vrUserInfo.getNewPassword();
    		try {
    			masterPassword = DESUtil.decryptString(masterPassword);
				m.setNewPassword(masterPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		masterSubAccountList.add(m);
    	}
    	
    	masterSubAccountList.addAll(tempList);
    	return Response.success(masterSubAccountList);
    }
    
    
    
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation("主子账号解绑")
    public Response deleteMasterSubAccount(@RequestBody @Valid MasterSubAccountProto.RequestDelete request){
        try{
        	String id = request.getId();
        	masterSubAccountService.delete(id);
        	return Response.success("解绑成功");
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation("主子账号绑定")
    public Response addMasterSubAccount(@RequestBody @Valid MasterSubAccountProto.RequestAddMasterSubAccount request){
        try{
        	MasterSubAccount m = new MasterSubAccount();
        	m.setId(JavaUUIDGenerator.get());
        	String userId = ShiroUtils.getLoginUserId();
        	VrUserInfo masterVrUserInfo = vrUserInfoService.get(userId);
        	m.setMasterPhoneNum(masterVrUserInfo.getPhoneNum());
        	String phoneNum = request.getPhoneNum();
        	m.setSubPhoneNum(phoneNum);
        	
        	if(masterVrUserInfo.getPhoneNum().equals(phoneNum)) {
        		return Response.error("不能绑定自己",1109);
        	}
        	
        	VrUserInfo subVrUserInfo = vrUserInfoService.findByPhoneNum(phoneNum);
        	if(subVrUserInfo == null) {
        		return Response.error("不存在此账号");
        	}
        	String password = request.getPassword();
        	password = DESUtil.encryptString(password);
        	if(!password.equals(subVrUserInfo.getNewPassword())) {
        		return Response.error("密码错误",1202);
        	}
        	
        	
        	MasterSubAccount dbMasterSubAccount1 = masterSubAccountService.findBySubPhoneNum(m.getSubPhoneNum());
        	if(dbMasterSubAccount1 != null) {
        		return Response.error("此账号被"+dbMasterSubAccount1.getMasterPhoneNum()+"账号绑定，请通知解绑，再添加");
        	}
        	
        	MasterSubAccount dbMasterSubAccount2 = masterSubAccountService.findBySubPhoneNum(m.getMasterPhoneNum());
        	if(dbMasterSubAccount2 != null) {
        		return Response.error("子账号不可以绑定");
        	}
        	
        	masterSubAccountService.add(m);
        	return Response.success("绑定成功");
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }
}
