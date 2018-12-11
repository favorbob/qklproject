/**
* generator by mybatis plugin Tue Jul 10 16:34:11 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.modules.eth.dao.EthIntegralAmplifyMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthIntegralAmplify;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EthIntegralAmplifyService extends DataStoreServiceImpl<String, EthIntegralAmplify, EthIntegralAmplifyMapper> {
    public EthIntegralAmplify getByTransactionHash(String transactionHash) {
        EthIntegralAmplify query = new EthIntegralAmplify();
        query.setTransactionHash(transactionHash);
        return findOne(query);
    }

    public List<EthIntegralAmplify> getByUserId(String userId) {
        EthIntegralAmplify query = new EthIntegralAmplify();
        query.setUserId(userId);
        query.setStatus(1);
        return findList(query);
    }
}