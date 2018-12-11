package cc.stbl.token.innerdisc.eth.contracts.deploy;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.eth.Web3Properties;
import cc.stbl.token.innerdisc.eth.contracts.Dispatcher;
import cc.stbl.token.innerdisc.modules.eth.entity.EthContractDeployed;
import cc.stbl.token.innerdisc.modules.eth.service.EthContractDeployedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;

import java.io.IOException;
import java.util.Date;

@Component
public class DispatcherManager {

    @Autowired
    private Admin admin;

    @Autowired
    private Credentials sysCredentials;

    @Autowired
    private EthContractDeployedService deployedService;

    @Autowired
    private Web3Properties web3Properties;

    private Logger logger = LoggerFactory.getLogger(VrTokenManager.class);

    public Dispatcher deployed(String loginUserId, String targetAddress){
        try {
            Dispatcher token = Dispatcher.deploy(admin,sysCredentials,web3Properties.getGasLimit(),web3Properties.getGasPrice(),targetAddress).send();
            EthContractDeployed deployed = new EthContractDeployed();
            deployed.setId(JavaUUIDGenerator.get());
            deployed.setAtBlockNumber(getLastBlockNumber());
            deployed.setClazz(Dispatcher.class.getName());
            deployed.setContractAt(token.getContractAddress());
            deployed.setDeployDate(new Date());
            deployed.setDeployOwner(loginUserId);
            deployed.setDeployArgs(targetAddress);
            deployedService.add(deployed);
            return token;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public Long getLastBlockNumber() {
        try {
            return admin.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Dispatcher getLastDispatcher(){
        return getLastDispatcher(sysCredentials);
    }

    public Dispatcher getLastDispatcher(Credentials userCredentials){
        EthContractDeployed deployed = getLastDeployed();
        if(deployed == null) return null;
        return Dispatcher.load(deployed.getContractAt(),admin,userCredentials,web3Properties.getGasLimit(),web3Properties.getGasPrice());
    }

    public EthContractDeployed getLastDeployed(){
        return deployedService.getLastDeployed(Dispatcher.class.getName());
    }

}
