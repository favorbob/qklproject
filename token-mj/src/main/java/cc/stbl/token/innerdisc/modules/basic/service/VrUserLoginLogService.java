/**
* generator by mybatis plugin Wed Aug 22 16:03:36 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.VrUserLoginLogMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserLoginLog;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VrUserLoginLogService extends DataStoreServiceImpl<String, VrUserLoginLog, VrUserLoginLogMapper> {
}