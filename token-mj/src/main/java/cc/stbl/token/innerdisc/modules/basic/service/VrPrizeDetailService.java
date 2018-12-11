/**
* generator by mybatis plugin Thu Sep 27 10:50:19 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.basic.dao.VrPrizeDetailMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenTradeService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.common.DateUtil;
import cc.stbl.token.innerdisc.restapi.app.redenvelope.RedEnvelopeProto;

import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VrPrizeDetailService extends DataStoreServiceImpl<String, VrPrizeDetail, VrPrizeDetailMapper> {

	
    @Autowired
    private VrTokenManager vrTokenManager;
    @Autowired
    private EthWalletService ethWalletService;
    
    @Autowired
    private VrTokenTradeService vrTokenTradeService;
	
	/**
	 * 根据用户和时间查询红包
	 * @param userId
	 * @return
	 */
	public VrPrizeDetail selectByUserIdAndTime(String userId,String createTime) {
		return mapper.selectByUserIdAndTime(userId,createTime);
	}
	
	
	/**
	 * 获取红包
	 * @param userId
	 * @return
	 */
	@Transactional
	public RedEnvelopeProto.ResponseGetRedEnvelope getRedEnvelope(String userId) {
		RedEnvelopeProto.ResponseGetRedEnvelope response = new RedEnvelopeProto.ResponseGetRedEnvelope();
		String date = DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD);
    	String beginTime = date+" 8:0:0";
    	String now = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    	boolean flag = DateUtil.compare(beginTime,now);
    	String createTime = "";
    	if(flag) {
    		createTime = date;
    		logger.info("======service=====createTime1=="+createTime);
    	}else {
    		logger.info("=====service======createTime2=="+createTime);
    		throw new StructWithCodeException(ResponseCode.RED_ENVELOPE_NOT_ALLOW);
    	}
    	
    	VrPrizeDetail vrPrizeDetail = this.selectByUserIdAndTime(userId, createTime);
    	if(vrPrizeDetail == null) {
    		throw new StructWithCodeException(ResponseCode.RED_ENVELOPE_NOT_ALLOW);
    	}
    	BigDecimal originalAiic = vrPrizeDetail.getOriginalAiic();
    	BigDecimal afterAiic = vrPrizeDetail.getAfterAiic();
    	BigDecimal originalMj = vrPrizeDetail.getOriginalMj();
    	BigDecimal afterMj = vrPrizeDetail.getAfterMj();
    	BigDecimal originalAsset = vrPrizeDetail.getOriginalAsset();
    	BigDecimal afterAsset = vrPrizeDetail.getAfterAsset();
    	
    	BigDecimal aiic = afterAiic.subtract(originalAiic).setScale(4, BigDecimal.ROUND_HALF_UP);
    	BigDecimal mj = afterMj.subtract(originalMj).setScale(4, BigDecimal.ROUND_HALF_UP);
    	BigDecimal integral = originalAsset.subtract(afterAsset).setScale(4, BigDecimal.ROUND_HALF_UP);
    	BigDecimal integralRet = integral.setScale(2, RoundingMode.HALF_UP);
    	String id = vrPrizeDetail.getId();
    	
    	 //mj
        EthWallet userWallet = ethWalletService.getUserWallet(userId);
        BigInteger limitBalanceOf = vrTokenManager.limitBalanceOf(userWallet.getAddress());
        BigDecimal allMj = UnitConvertUtils.toEther(limitBalanceOf).setScale(2, RoundingMode.HALF_UP);
        allMj = allMj.add(mj);
        
        BigDecimal tempMj = mj.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tempAllMj = allMj.setScale(2, RoundingMode.HALF_UP);
       
        BigInteger bigInteger = vrTokenManager.integralOf(userWallet.getAddress());
        BigDecimal bigDecimalIntegral = UnitConvertUtils.toEther(bigInteger).setScale(2, RoundingMode.HALF_UP);
        integral = bigDecimalIntegral.subtract(integral).setScale(2, RoundingMode.HALF_UP);
        
        response.setMj(tempMj.toString());
        response.setAllMj(tempAllMj.toString());
        response.setIntegral(integral.toString());
        
        //aiic
        BigInteger balanceOf = vrTokenManager.balanceOf(userWallet.getAddress());
        BigDecimal allAiic = UnitConvertUtils.toEther(balanceOf).setScale(2, RoundingMode.HALF_UP);
        allAiic = allAiic.add(aiic);
        
        BigDecimal tempAiic = aiic.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tempAllAiic = allAiic.setScale(2, RoundingMode.HALF_UP);
        response.setAiic(tempAiic.toString());
        response.setAllAiic(tempAllAiic.toString());
    	
        VrPrizeDetail vrPrizeDetailNew = new VrPrizeDetail();
        vrPrizeDetailNew.setId(vrPrizeDetail.getId());
        vrPrizeDetailNew.setSettleCount(true);
        vrPrizeDetailNew.setSettleDate(new Date());
		this.update(vrPrizeDetailNew);
		
        //次数
        Integer time = this.countByUserId(userId);
        response.setTime(time);
        
        //调用区块链接口
//        vrTokenTradeService.restAiic(userId, afterAiic, JavaUUIDGenerator.get());
//        vrTokenTradeService.restMj(userId, integral, JavaUUIDGenerator.get());
//        vrTokenTradeService.restAsset(userId, integral,JavaUUIDGenerator.get());
        logger.info("mj aiic integral :{}   :{}   :{}",mj,aiic,integral);
    	vrTokenTradeService.integralReturn(userId, aiic, mj, integralRet, id);
    	return response;
	}
	
	
	/**
	 * 根据userid统计领取红包次数
	 * @param userId
	 * @return
	 */
	public int countByUserId(String userId) {
		return mapper.countByUserId(userId);
	}
	
	/**
	 * 根据userid统计mj和aiic
	 * @param userId
	 * @return
	 */
	public Map<String,BigDecimal> countMjAndAiicByUserId(String userId){
		return mapper.countMjAndAiicByUserId(userId);
	}

	/**
	 * 分页查询奖金明细
	 * @param query 查询对象
	 * @param pageNo
	 * @param pageSize
	 * @return 分页结果
	 */
	public Pager<VrPrizeDetail> findPageVrPrizeDetail(VrPrizeDetail query, Integer pageNo, Integer pageSize) {
		Long total = this.mapper.findVrPrizeDetailCount(query);
		Integer offset = (pageNo - 1) * pageSize;
		if(total == 0) {
			return new Pager<VrPrizeDetail>(pageNo,pageSize,new ArrayList<VrPrizeDetail>(),total);
		}
		RowBounds rowBounds = new RowBounds(offset,pageSize) ;
		List<VrPrizeDetail> list = this.mapper.findVrPrizeDetailList(query, rowBounds); //根据会员编号和日期查询list
		return new Pager<VrPrizeDetail>(pageNo,pageSize,list,total);
	}

	/**
	 * 查询奖金明细 集合
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<VrPrizeDetail> findListVrPrizeDetail(VrPrizeDetail query, Integer pageNo, Integer pageSize) {
		Integer offset = (pageNo - 1) * pageSize;
		RowBounds rowBounds = new RowBounds(offset,pageSize) ;
		List<VrPrizeDetail> list = this.mapper.findVrPrizeDetailList(query, rowBounds); //根据会员编号和日期查询list
		return list;
	}
}