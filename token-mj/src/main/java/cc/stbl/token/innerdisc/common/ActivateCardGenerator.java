package cc.stbl.token.innerdisc.common;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.stbl.token.innerdisc.modules.sys.entity.ActivateCard;
import cc.stbl.token.innerdisc.modules.sys.service.ActivateCardSevice;

@Component
public class ActivateCardGenerator {
	
	@Autowired
	ActivateCardSevice service;

	/**
	 * 根据激活卡类型，生成指定数量的激活卡号
	 * @param activateCardType 类型 <br/>公司生成：ActivateCardGenerator.ActivateCardType.GS<br/>会员生成： ActivateCardGenerator.ActivateCardType.HY
	 * @param num 激活卡数量
	 * @return 指定数量的激活卡
	 */
	public Set<String> get(ActivateCardType activateCardType,int num){
		if(activateCardType == null || num < 1){
			return null;
		}
		
		//卡号规则： 2位卡号前缀 + 6位批次序号 + 6位随机数 + 1位校验码
		String seq = null;
		while (true){
			seq = genRandomStr(6);
			ActivateCard card = new ActivateCard();
			card.setCreateAt(new Date());
			card.setNum(num);
			card.setSeq(seq);
			int row;
			try {
				row = service.insert(card);
			} catch (Exception e) {
				row = 0;
			}
			if(row == 1){
				break;
			}
		}
		
		Set<String> set = new HashSet<>(num);
		String type = activateCardType.get();
		
		while(set.size()<num){
			String code = type + seq + genRandomStr(6);
			String verify = genVerifyCode(code);
			set.add(code + verify);
		}
		
		return set;
	}
	
	public static enum ActivateCardType{
		//公司生成的激活卡前缀
		GS("GS"),
		//会员生成的激活卡前缀
		HY("HY");
		
		ActivateCardType(String value){
			this.value = value;
		}
		
		private String value;

		public String get() {
			return value;
		}
		
	}
	
	private static final String[] ARR = { "P", "H", "I", "3", "B", "4", "X", "D", "E", "W", "8", "Q", "9", "Y", "F",
			"G", "K", "A", "L", "C", "N", "7", "R", "S", "5", "T", "M", "2", "U", "V", "Z", "1", "J", "6" };

	private static final int ARR_SIZE = ARR.length;

	private static Random random = new Random();

	public static String genRandomStr(int size) {
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			sb.append(ARR[random.nextInt(ARR_SIZE)]);
		}
		return sb.toString();
	}
	
	/**
	 * 根据激活码前N位，生成最后一位校验位
	 * 
	 * @param source 激活码前N位（目前是14位）
	 * @return 校验位
	 */
	public static String genVerifyCode(String source) {
		if (StringUtils.isBlank(source) || source.length() != 14) {
			return "";
		}
		return ARR[hashCode(source)]; // 1位校验码
	}
	
	private static int hashCode(String str) {
		int hc = str.hashCode();
		hc += str.substring(5, 10).hashCode();
		if (hc < 0) {
			hc = hc * -1;
		}
		return hc % ARR_SIZE;
	}
	
	/**
	 * 校验激活码是否合法
	 * 
	 * @param code 15位激活码
	 * @return true-合法，false-非法
	 */
	public static boolean verifyCode(String code) {
		if (StringUtils.isBlank(code) || code.length() != 15) {
			return false;
		}
		String genCode = genVerifyCode(code.substring(0, 14));
		return genCode.equals(code.substring(14));
	}

}
