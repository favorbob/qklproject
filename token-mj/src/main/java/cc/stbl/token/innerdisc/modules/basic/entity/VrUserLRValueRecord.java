package cc.stbl.token.innerdisc.modules.basic.entity;

import java.util.Date;

import lombok.Data;


@Data
public class VrUserLRValueRecord {
	
	//表示没有处理过
	public static Integer STATUS_0 = 0;
	//表示已经处理过
	public static Integer STATUS_1 = 1;
	
	
	private String id;
	
    /**
    * 字段：vr_user_lrvalue.user_id；备注：用户id
     */
    private String userId;

    /**
     * 状态  0表示没有处理过业绩，1表示是处理过业绩
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 添加的值
     */
    private String addValue;
}