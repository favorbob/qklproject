package cc.stbl.token.innerdisc.modules.basic.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.modules.basic.dao.MasterSubAccountMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.MasterSubAccount;

/**
 * 主子账号服务类
 * @author fyf
 *
 */
@Service
public class MasterSubAccountService extends DataStoreServiceImpl<String,MasterSubAccount,MasterSubAccountMapper> {

	
	public List<MasterSubAccount> findListBySubPhoneNum(String subPhoneNum){
		return mapper.findListBySubPhoneNum(subPhoneNum);
	}

	/**
	 * 通过子账号查找实体
	 * @param subPhoneNum
	 * @return
	 */
	public MasterSubAccount findBySubPhoneNum(String subPhoneNum) {
		return mapper.findBySubPhoneNum(subPhoneNum);
	}
}
