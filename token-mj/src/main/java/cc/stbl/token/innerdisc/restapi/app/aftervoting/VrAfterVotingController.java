package cc.stbl.token.innerdisc.restapi.app.aftervoting;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.util.DESUtil;
import cc.stbl.token.innerdisc.modules.basic.common.AreaUtil;
import cc.stbl.token.innerdisc.modules.basic.entity.VrAfterVoting;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrAfterVotingService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.sys.entity.SysProperties;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/user/afterVoting")
@Api(description = "复投模块")
public class VrAfterVotingController {

	@Autowired
	private VrAfterVotingService vrAfterVotingService;
	@Autowired
	private VrUserInfoService vrUserInfoService;
	@Autowired
	private SysPropertiesService sysPropertiesService;
	
	@Autowired
	private AreaUtil areaUtil;
    
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("复投")
    @ResponseBody
    public Response afterVoting(@RequestBody @Valid VrAfterVotingProto.AfterVotingRequest request){
    	int num = request.getNum();
    	if(num==0) {
    		return Response.error("复投数量不可以为0");
    	}
    	
    	String leastAfterVotingNum = sysPropertiesService.getString("sys","least.after.voting.num");
    	
    	if(num<Integer.parseInt(leastAfterVotingNum)) {
    		return Response.error("复投数量太少");
    	}
    	
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo user = vrUserInfoService.get(userId);
    	
    	String payPassword = request.getPayPassword();
    	String dbPayPassword="";
		try {
			dbPayPassword = DESUtil.decryptString(user.getPayPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(!payPassword.equals(dbPayPassword)) {
    		Response.error("支付密码错误");
    	}
    	Long aArea = user.getaArea();
    	Long bArea = user.getbArea();
    	String phoneNum = user.getPhoneNum();
    	int multiple = areaUtil.getAreaMultiple(aArea, bArea);
    	vrAfterVotingService.afterVoting(userId,payPassword,phoneNum,multiple,num);
        return Response.success("复投成功");
    }
    
    
    @RequestMapping(value = {"/list"},method = RequestMethod.GET)
    @ApiOperation("转账列表")
    @ResponseBody
    public Response<Pager<VrAfterVoting>> list(@RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize){
    	VrAfterVoting query = new VrAfterVoting();
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo user = vrUserInfoService.get(userId);
    	query.setPhoneNum(user.getPhoneNum());
        Pager<VrAfterVoting> pager = vrAfterVotingService.findPage(query,pageNo,pageSize);
        return Response.success(pager);
    }
    
    
    @RequestMapping(value = {"/getLeastAfterVotingNum"},method = RequestMethod.GET)
    @ApiOperation("复投最少数量")
    @ResponseBody
    public Response getLeastAfterVotingNum() {
    	String leastAfterVotingNum = sysPropertiesService.getString("sys","least.after.voting.num");
    	return Response.success(leastAfterVotingNum);
    }
}
