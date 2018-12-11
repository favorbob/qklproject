package cc.stbl.token.innerdisc.restapi.app.redenvelope;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.VrPrizeDetailService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.app.common.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/redenvelope")
@Api(description = "红包")
public class RedEnvelopeController {

    public static Logger logger = LoggerFactory.getLogger(RedEnvelopeController.class);

    @Autowired
    private VrUserInfoService vrUserInfoService;
    
    @Autowired
    private VrPrizeDetailService vrPrizeDetailService;

    @RequestMapping(value = "/getRedEnvelope",method = RequestMethod.GET)
    @ApiOperation("获取红包")
    public Response getRedEnvelope(){
  
    	RedEnvelopeProto.ResponseGetRedEnvelope response = new RedEnvelopeProto.ResponseGetRedEnvelope();
    	
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo vrUserInfo = vrUserInfoService.get(userId);
    	if(vrUserInfo.getStatus() != VrUserInfo.USER_STATUS_NORMAL) {
    		return Response.error("用户状态不正常");
    	}
        try{
        	
        	response = vrPrizeDetailService.getRedEnvelope(userId);
        	return Response.success(response);
        }catch (Exception e){
        	logger.error(e.getMessage(),e);
            return Response.error("领取失败");
        }
    }
    
    @RequestMapping(value = "/canGetRedEnvelope",method = RequestMethod.GET)
    @ApiOperation("查询今天能不能获取红包")
    public Response canGetRedEnvelope(){
  
    	String userId = ShiroUtils.getLoginUserId();
    	VrUserInfo vrUserInfo = vrUserInfoService.get(userId);
    	if(vrUserInfo.getStatus() != VrUserInfo.USER_STATUS_NORMAL) {
    		return Response.error("用户状态不正常");
    	}
        try{
        	String date = DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD);
        	String beginTime = date+" 8:0:0";
        	String now = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        	boolean flag = DateUtil.compare(beginTime,now);
        	String createTime = "";
        	if(flag) {
        		createTime = date;
        		logger.info("===========createTime=="+createTime);
        	}else {
        		
        		//createTime = DateUtil.getYesteday();
        		logger.info("===========createTime=="+createTime);
        		return Response.error("没有红包");
        	}
        	
        	VrPrizeDetail vrPrizeDetail = vrPrizeDetailService.selectByUserIdAndTime(userId, createTime);
        	if(vrPrizeDetail !=null ) {
        		//表示可以领取好吧
        		return Response.success(true);
        	}else {
        		//表示没有红包可以领取
        		return Response.error("没有红包");
        	}
        }catch (Exception e){
            return Response.error("查询失败");
        }
    }
   
    
    @RequestMapping(value = "/redEnvelopeStatistical",method = RequestMethod.GET)
    @ApiOperation("红包统计")
    public Response redEnvelopeStatistical(RedEnvelopeProto.RequestRedEnvelopeDetail request){
  
    	String userId = ShiroUtils.getLoginUserId();
        try{
        	RedEnvelopeProto.ResponseRedEnvelopeDetail response = new RedEnvelopeProto.ResponseRedEnvelopeDetail();
            //次数
            Integer time = vrPrizeDetailService.countByUserId(userId);
            response.setTime(time);
            Map<String,BigDecimal> VrPrizeDetailMap = vrPrizeDetailService.countMjAndAiicByUserId(userId);
            if(VrPrizeDetailMap != null) {
	            BigDecimal originalMj = VrPrizeDetailMap.get("originalMj");
	            BigDecimal afterMj = VrPrizeDetailMap.get("afterMj");
	            BigDecimal originalAiic = VrPrizeDetailMap.get("originalAiic");
	            BigDecimal afterAiic = VrPrizeDetailMap.get("afterAiic");
	            // mj 和aiic
	            BigDecimal mj = afterMj.subtract(originalMj);
	            BigDecimal aiic = afterAiic.subtract(originalAiic);
	            response.setCumulativeMj(mj.toString());
	            response.setCumulativeAiic(aiic.toString());
            }
            VrPrizeDetail query = new VrPrizeDetail();
            query.setUserId(userId);
            query.setSettleCount(true);
           
            Integer pageNo = request.getPageNo();
            Integer pageSize = request.getPageSize();
           
            Pager<VrPrizeDetail> pager = vrPrizeDetailService.findPage(query, pageNo, pageSize);
            if(pager !=null) {
            	List<VrPrizeDetail> vrPrizeDetailList = pager.getList();
            	for(VrPrizeDetail v:vrPrizeDetailList) {
            		v.setMj(v.getAfterMj().subtract(v.getOriginalMj()).setScale(2, RoundingMode.HALF_UP));
            		v.setAiic(v.getAfterAiic().subtract(v.getOriginalAiic()).setScale(2, RoundingMode.HALF_UP));
            	}
            }
            response.setPager(pager);
            return Response.success(response);
        }catch (Exception e){
        	logger.error(e.getMessage(),e);
            return Response.error("系统异常");
        }
    }
    
   
}
