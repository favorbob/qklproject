/**
* generator by mybatis plugin Tue Jul 10 10:27:57 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserAccountMaintainMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserAccountMaintain;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VrUserAccountMaintainService extends DataStoreServiceImpl<String, VrUserAccountMaintain, VrUserAccountMaintainMapper> {

	//跟换账户资产

	/**
	 * 会员账户维护-更换账户资产
	 * @param object 用户账户维护表
	 * 账户类型 1:资产,2:MJ, 3:AIIC
	 * @return
	 */
	public String changeAccountAsset(VrUserAccountMaintain object) {
        Integer acType = object.getAcType();        //操作类型  1:资产,2:MJ, 3:AIIC
        String afterNum = object.getAfterModife();  //更改后的金额
        if(acType == 1) {
            //修改资产后的内容

        }
        if(acType == 2) {
            //修改MJ资产后的内容
        }
        if(acType == 3) {
            //修改AIIC资产后的内容
        }
        object.setId(JavaUUIDGenerator.get());
        object.setCreateDate(new Date());
		super.add(object);  //记录资产更改情况
		return "更换账户资产成功！";
	}

}