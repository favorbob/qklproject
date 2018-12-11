package cc.stbl.token.innerdisc.modules.basic.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import ss.stbl.common.datastore.domain.OrderByQuery;

/**
 * 复投
 * @author fyf
 *
 */

@Data
public class VrAfterVoting extends OrderByQuery{

	/**
	 * 复投的时候上级添加的业绩
	 */
	public static String addValue = "20";
	
	private String id;
	private String phoneNum;
	private Date createTime;
	private int num;
	private int multiple;
	private BigDecimal beforeAsset;
	private BigDecimal afterAsset;
	private String remark;
	private String startDate;
	private String endDate;
}
