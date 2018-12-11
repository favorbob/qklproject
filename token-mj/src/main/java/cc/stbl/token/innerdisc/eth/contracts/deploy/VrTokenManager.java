package cc.stbl.token.innerdisc.eth.contracts.deploy;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.eth.Web3Properties;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.tx.EthNonceManager;
import cc.stbl.token.innerdisc.eth.tx.RedisNonceTransactionManager;
import cc.stbl.token.innerdisc.modules.eth.entity.EthContractDeployed;
import cc.stbl.token.innerdisc.modules.eth.service.EthContractDeployedService;
import cc.stbl.token.innerdisc.modules.eth.trades.ethevent.VrTokenEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

@Component
public class VrTokenManager  {

    @Autowired
    private Admin admin;

    @Autowired
    private Credentials sysCredentials;

    @Autowired
    private EthContractDeployedService deployedService;

    @Autowired
    private Web3Properties web3Properties;

    private Logger logger = LoggerFactory.getLogger(VrTokenManager.class);

    @Autowired
    private EthNonceManager ethNonceManager;

    @Autowired
    private VrTokenEventManager vrTokenEventManager;

    private RedisNonceTransactionManager nonceTransactionManager;

    public VRToken deployed(String loginUserId,String name, String symbol, BigInteger totalSupply){
        try {
            VRToken token = VRToken.deploy(admin,getNonceTransactionManager(),web3Properties.getGasLimit(),web3Properties.getGasPrice(),name,symbol,totalSupply).send();
            EthContractDeployed deployed = new EthContractDeployed();
            deployed.setId(JavaUUIDGenerator.get());
            deployed.setAtBlockNumber(getLastBlockNumber());
            deployed.setClazz(VRToken.class.getName());
            deployed.setContractAt(token.getContractAddress());
            deployed.setDeployDate(new Date());
            deployed.setDeployOwner(loginUserId);
            deployed.setDeployArgs(name + "#$#" + symbol + "#$#" + totalSupply.toString());
            deployedService.add(deployed);
            vrTokenEventManager.startListener(deployed,token);
            return token;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public VRToken getLastVrToken(){
        return getLastVrToken(sysCredentials);
    }

    public EthContractDeployed getLastDeployed(){
        return deployedService.getLastDeployed(VRToken.class.getName());
    }

    public Long getLastBlockNumber() {
        try {
            return admin.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public synchronized RedisNonceTransactionManager getNonceTransactionManager() {
        if(this.nonceTransactionManager == null)  this.nonceTransactionManager = new RedisNonceTransactionManager(admin,sysCredentials,ethNonceManager);
        return this.nonceTransactionManager;
    }

    public VRToken loadAdminVrToken(String address){
        return VRToken.load(address,admin,getNonceTransactionManager(),web3Properties.getGasLimit(),web3Properties.getGasPrice());
    }

    private VRToken getLastVrToken(Credentials userCredentials){
        EthContractDeployed deployed = getLastDeployed();
        if(deployed == null) return null;
        return VRToken.load(deployed.getContractAt(),admin,getNonceTransactionManager().getUserNonceTransactionManager(userCredentials),web3Properties.getGasLimit(),web3Properties.getGasPrice());
    }
    //用户冻结资产
    public BigInteger lockedBalanceOf(String address) {
        VRToken vrToken = getLastVrToken();
        return lockedBalanceOf(address,vrToken);
    }

    //用户冻结资产
    public BigInteger lockedBalanceOf(String address, VRToken vrToken){
        try {
            return vrToken.lockedBalanceOf(address).send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }

    //用户 aiic
    public BigInteger balanceOf(String address) {
        VRToken vrToken = getLastVrToken();
        return balanceOf(address,vrToken);
    }
    //用户 aiic
    public BigInteger balanceOf(String address, VRToken vrToken){
        try {
            return vrToken.balanceOf(address).send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }
    //用户 冻结+ mj + aiic
    public BigInteger totalBalanceOf(String address){
        return totalBalanceOf(address,getLastVrToken());
    }

    //用户 冻结+ mj + aiic
    public BigInteger totalBalanceOf(String address, VRToken vrToken) {
        try {
            return vrToken.totalBalanceOf(address).send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }

    //用户资产
    public BigInteger integralOf(String address){
        return integralOf(address,getLastVrToken());
    }

    //用户资产
    public BigInteger integralOf(String address, VRToken vrToken) {
        try {
            return vrToken.integralOf(address).send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }

    //发币初始值
    public BigInteger initTotalSupply(VRToken vrToken) {
        try {
            return vrToken.initTotalSupply().send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }

    //用户 MJ 资产
    public BigInteger limitBalanceOf(String address,VRToken vrToken) {
        try {
            return vrToken.limitedBalanceOf(address).send();
        } catch (Exception e) {
            return new BigInteger("0");
        }
    }
    //用户 MJ 资产
    public BigInteger limitBalanceOf(String address) {
        return limitBalanceOf(address,getLastVrToken());
    }
    //是否可以挖矿
    public boolean canMint(){return canMint(getLastVrToken());}
    //是否可以挖矿
    public boolean canMint(VRToken vrToken) {
        try {
            return !vrToken.mintingFinished().send();
        } catch (Exception e) {
            return false;
        }
    }

}
