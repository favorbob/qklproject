package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

import lombok.Data;
import ss.stbl.common.datastore.domain.OrderByQuery;

/**
 * 交易实体  mj和Aiic
 * @author fyf
 *
 */

@Data
public class VrUserTrade extends OrderByQuery{

	public static Integer TRADE_TYPE_MJ = 1;
	public static Integer TRADE_TYPE_AIIC = 2;
	public static Integer TRADE_STATUS_COMPLETE = 3;
	
	private String id;
	private String inAccount;
	private String outAccount;
	private Integer tradeType;
	private Integer tradeNum;
	private Date updateTime;
	private Integer status;
	private String remark;
	private String image1;
	private String image2;
	private String image3;
	
	private String beginTime;
    private String endTime;
}
