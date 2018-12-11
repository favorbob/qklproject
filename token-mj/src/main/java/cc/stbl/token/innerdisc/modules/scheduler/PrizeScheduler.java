package cc.stbl.token.innerdisc.modules.scheduler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cogent.cache.CacheService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.MJAward;
import cc.stbl.token.innerdisc.common.MJAward.User;
import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLRValue;
import cc.stbl.token.innerdisc.modules.basic.service.VrPrizeDetailService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserLRValueService;
import cc.stbl.token.innerdisc.modules.payment.service.MyAssetsService;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.restapi.app.payment.MyAssetsProto;

/**
 * MJ奖励统计定时统计任务
 */
public class PrizeScheduler implements SimpleJob {

	// 每天 00:00:01 开始执行
	@Override
	public void execute(ShardingContext shardingContext) {
		try { // 先睡一下，以便几个并发线程能稍微错开一下开始，减少同时put同一个key到缓存的概率
			Thread.sleep(random.nextInt(2345));
		} catch (Exception e) {
		}
		logger.debug("PrizeScheduler execute start...{}..", shardingContext.getShardingItem());
		long t1 = System.currentTimeMillis();

		// 奖金释放比率MJ
		BigDecimal mjRatio = new BigDecimal(proServive.getStringFromDB(kind, mjRatio_key));
		// 奖金释放比率AIIC
		BigDecimal aiicRatio = new BigDecimal(proServive.getStringFromDB(kind, aiicRatio_key));
		// AIIC价格
		BigDecimal aiicPrice = new BigDecimal(proServive.getStringFromDB(kind, aiicPrice_key));

		String minLevel = proServive.getStringFromDB(kind, "minLevel", "2");
		String maxLevel = proServive.getStringFromDB(kind, "maxLevel", "20");
		String localDate = LocalDate.now().toString();
		// 查询需要统计mj奖励的用户列表
		List<VrUserLRValue> prizeUserList = lrService.getPrizeUserList(Integer.parseInt(minLevel), localDate);
		if(CollectionUtils.isEmpty(prizeUserList)){
			return;
		}
		prizeUserList.stream().filter(ul -> {
			int idHashCode = ul.getUserId().hashCode();
			if (idHashCode < 0) {
				idHashCode = idHashCode * -1;
			}
			return idHashCode % shardingContext.getShardingTotalCount() == shardingContext.getShardingItem();
		}).forEach(u -> {
			logger.debug("===============PrizeScheduler forEach userid.{}..{}..", u.getUserId(),
					shardingContext.getShardingItem());
			JSONObject asset = getAsset(u.getUserId());
			String integralStr = asset.getString("integral");// 资产
			BigDecimal integral = new BigDecimal(integralStr);
			logger.debug("用户id：{}，资产:{}", u.getUserId(), integralStr);
			if(integral.compareTo(new BigDecimal("0")) > 0){ //只有资产大于0时，才能发放mj和aiic
				List<VrUserLRValue> subLevelUserList = lrService.getSubList(u, Integer.parseInt(maxLevel),1,2);
				List<User> users = new ArrayList<>();

				for (VrUserLRValue su : subLevelUserList) {

					JSONObject asset1 = getAsset(su.getUserId());

					String integralStr1 = asset1.getString("integral");// 资产
					BigDecimal integral1 = new BigDecimal(integralStr1);
					BigDecimal ratio = new BigDecimal(getReleaseRatio(integral1)); // 资产释放率
					// 当天释放的MJ值
					BigDecimal mj = integral1.multiply(ratio).multiply(mjRatio);
					String level = getLevel(integral1);
					// 第一层层级奖比例
					BigDecimal l1Rate = new BigDecimal(
							proServive.getStringFromDB(kind, String.format("%s.l1.ratio", level)));
					// 第二层层级奖比例
					BigDecimal l2Rate = new BigDecimal(
							proServive.getStringFromDB(kind, String.format("%s.l2.ratio", level)));
					// 第三层层级奖比例
					BigDecimal l3Rate = new BigDecimal(
							proServive.getStringFromDB(kind, String.format("%s.l3.ratio", level)));

					User user = new User(su.getUserLevel(), su.getLft(), su.getRgt(), mj, l1Rate, l2Rate, l3Rate);
					users.add(user);
				}

				BigDecimal b = MJAward.getAward(users);
				
				BigDecimal ratio = new BigDecimal(getReleaseRatio(integral)); // 资产释放率

				// 总静态收益:当日释放的资产总额
				BigDecimal totalEarning = integral.multiply(ratio);
				VrPrizeDetail detail = new VrPrizeDetail();
				detail.setId(JavaUUIDGenerator.get());
				detail.setUserId(u.getUserId());
				detail.setTotalEarning(totalEarning);
				detail.setMjEarning(totalEarning.multiply(mjRatio));
				detail.setAiicEarning(totalEarning.multiply(aiicRatio));
				detail.setLevelAward(b.subtract(detail.getMjEarning()));
				detail.setOriginalAsset(integral); // 原资产账户
				detail.setAfterAsset(detail.getOriginalAsset().subtract(totalEarning));//资产减静态收益
				
				BigDecimal afterAsset = detail.getAfterAsset();
				if(afterAsset.compareTo(detail.getLevelAward()) < 0){ //资产余额比层级奖要小的时候，只按余额发放层级奖
					detail.setLevelAward(afterAsset);
				}
				detail.setAfterAsset(afterAsset.subtract(detail.getLevelAward()));//资产减动态收益
				
				detail.setOriginalMj(new BigDecimal(asset.getString("restrictedAssets"))); // 原MJ账户
				detail.setAfterMj(detail.getOriginalMj().add(b));
				detail.setOriginalAiic(new BigDecimal(asset.getString("availableAssets"))); // 原AIIC账户
				detail.setAfterAiic(detail.getOriginalAiic().add(detail.getAiicEarning().divide(aiicPrice,2, BigDecimal.ROUND_HALF_UP)));
				detail.setSettleDate(new Date());
				detail.setCreateDate(localDate);
				service.add(detail);
			} else {
				logger.debug("用户id：{}，资产为0，今天没有红包", u.getUserId());
			}
		});
		logger.debug("PrizeScheduler execute end...{}... total time:{}", shardingContext.getShardingItem(),
				(System.currentTimeMillis() - t1));
	}

	public JSONObject getAsset(String userId) {
		String key = String.format("PrizeScheduler:myAssetsHome:%s", userId);

		String str = cache.get(key, String.class);
		if (StringUtils.isBlank(str)) {
			MyAssetsProto.ResponseMyAssetsSupports asset = assetsService.myAssetsHome(userId).getData();
			str = JSON.toJSONString(asset);
			cache.put(key, str, 2 * 60 * 60);
		}
		JSONObject json = JSONObject.parseObject(str);
		return json;
	}

	@Autowired
	private VrPrizeDetailService service;

	@Autowired
	private VrUserLRValueService lrService;

	@Autowired
	private SysPropertiesService proServive;

	@Autowired
	private MyAssetsService assetsService;

	@Autowired
	private CacheService cache;

	private static Random random;

	// 根据用户资产计算其对应的资产释放比率
	private String getReleaseRatio(BigDecimal integral) {
		String level = getLevel(integral);
		return proServive.getStringFromDB(kind, String.format("%s.ratio", level));
	}

	private String getLevel(BigDecimal integral) {
		String level = V1;

		BigDecimal v1 = new BigDecimal(proServive.getStringFromDB("sys", V1));
		BigDecimal v2 = new BigDecimal(proServive.getStringFromDB("sys", V2));
		BigDecimal v3 = new BigDecimal(proServive.getStringFromDB("sys", V3));
		BigDecimal v4 = new BigDecimal(proServive.getStringFromDB("sys", V4));
		if (integral.compareTo(v1) >= 0 && integral.compareTo(v2) < 0) {
			level = V2;
		} else if (integral.compareTo(v2) >= 0 && integral.compareTo(v3) < 0) {
			level = V3;
		} else if (integral.compareTo(v4) >= 0) {
			level = V4;
		}
		return level;
	}

	private static final String V1 = "v1";
	private static final String V2 = "v2";
	private static final String V3 = "v3";
	private static final String V4 = "v4";

	private static final String mjRatio_key = "release.mj.ratio";
	private static final String aiicRatio_key = "release.aiic.ratio";
	private static final String aiicPrice_key = "aiic.price";
	private static final String kind = "prize";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
