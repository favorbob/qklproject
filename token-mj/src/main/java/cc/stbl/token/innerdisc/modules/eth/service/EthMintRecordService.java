/**
* generator by mybatis plugin Mon Aug 20 17:30:48 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.modules.eth.dao.EthMintRecordMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EthMintRecordService extends DataStoreServiceImpl<String, EthMintRecord, EthMintRecordMapper> {
}