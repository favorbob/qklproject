/**
* generator by mybatis plugin Mon Jul 09 15:28:01 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.modules.eth.dao.EthContractDeployedMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthContractDeployed;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EthContractDeployedService extends DataStoreServiceImpl<String, EthContractDeployed, EthContractDeployedMapper> {
    public EthContractDeployed getLastDeployed(String clazz) {
        EthContractDeployed query = new EthContractDeployed();
        query.setStatus(1);
        query.setClazz(clazz);
        return findOne(query);
    }
}