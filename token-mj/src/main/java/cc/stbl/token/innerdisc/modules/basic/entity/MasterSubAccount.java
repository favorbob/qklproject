package cc.stbl.token.innerdisc.modules.basic.entity;

import lombok.Data;

/**
 * 主子账户
 * @author fyf
 *
 */

@Data
public class MasterSubAccount {

	private String id;
	private String masterPhoneNum;
	private String subPhoneNum;
	private String newPassword;
	
	
	
}
