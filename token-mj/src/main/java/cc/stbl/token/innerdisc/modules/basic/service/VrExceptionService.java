/**
* generator by mybatis plugin Mon Aug 13 18:19:01 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.modules.basic.dao.VrExceptionMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.VrException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VrExceptionService extends DataStoreServiceImpl<String, VrException, VrExceptionMapper> {
}