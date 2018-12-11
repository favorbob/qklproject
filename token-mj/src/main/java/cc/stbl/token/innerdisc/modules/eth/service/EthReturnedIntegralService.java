/**
* generator by mybatis plugin Tue Jul 10 17:33:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.modules.eth.dao.EthReturnedIntegralMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthReturnedIntegral;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EthReturnedIntegralService extends DataStoreServiceImpl<String, EthReturnedIntegral, EthReturnedIntegralMapper> {

    /**
     * 重新发送失败的记录
     */
    public void retryCommitLoseTx(){


    }
}