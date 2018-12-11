package cc.stbl.token.innerdisc.restapi.app.redenvelope;


import javax.validation.constraints.NotNull;

import com.ks.common.datastore.model.Pager;

import cc.stbl.token.innerdisc.modules.basic.entity.VrPrizeDetail;
import lombok.Data;

/**
 * 红包
 * 
 * @author fyf
 *
 */
public class RedEnvelopeProto {

	/**
	 * 领取红包返回
	 * 
	 * @author fyf
	 *
	 */
	@Data
	public static class ResponseGetRedEnvelope {
		private String mj;
		private String aiic;
		private String allMj;
		private String allAiic;
		private Integer time;
		private String integral;
	}

	/**
	 * 红包详情返回
	 */
	@Data
	public static class ResponseRedEnvelopeDetail {
		private String cumulativeMj;
		private String cumulativeAiic;
		private Integer time;
		Pager<VrPrizeDetail> pager;
	}

	/**
	 * 红包详情返回
	 */
	@Data
	public static class RequestRedEnvelopeDetail {
		@NotNull
		private Integer pageNo;
		@NotNull
		private Integer pageSize;
	}

}
