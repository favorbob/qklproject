package cc.stbl.token.innerdisc.modules.eth.trades.ethevent;

import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.modules.eth.entity.EthContractDeployed;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import com.cogent.cache.lock.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;

public abstract class AbstractVrTokenEvent  {

    @Autowired
    protected VrTokenManager vrTokenManager;

    @Autowired
    protected Admin admin;

    @Autowired
    private SysPropertiesService propertiesService;

    @Autowired
    protected DistributedLock distributedLock;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void listenerVrTokenEvent(EthContractDeployed deployed,VRToken vrToken){
//        EthContractDeployed deployed = vrTokenManager.getLastDeployed();
        if(deployed == null) return;
         Long storeStart = propertiesService.getLong("vrTokenEvent",getClass().getName());
        if(storeStart == null)  {
            storeStart = deployed.getAtBlockNumber() - 1;
            setStartValue(storeStart);
        }
        final Long _storeStart = storeStart;
        new Thread(() -> listenerEventStartBy(_storeStart,vrToken)).start();
    }

    public void setStartValue(Long start){
        if(start == null) return;
        Long storeStart = propertiesService.getLong("vrTokenEvent",getClass().getName());
        if(storeStart == null || start > storeStart)
            propertiesService.setProperties("vrTokenEvent",getClass().getName(),start.toString());
    }

    public EthBlock.Block getBlock(BigInteger blockNum){
        try {
            EthBlock block = admin.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNum),false).send();
            return block.getBlock();
        } catch (IOException e) {
            return null;
        }
    }
    protected abstract void listenerEventStartBy(Long start, VRToken vrToken);
}
