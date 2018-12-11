package cc.stbl.token.innerdisc.eth.tx;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.eth.Web3Properties;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import com.cogent.cache.lock.DistributedLock;
import com.cogent.cache.redis.RedisCacheService;
import com.stbl.framework.encoder.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * @author leon
 * @Date 2018/10/15
 */
@Component
public class EthNonceManager implements InitializingBean{

    @Autowired
    private Web3Properties web3Properties;

    @Autowired
    private Admin admin;

    @Autowired
    private Credentials sysCredentials;

    @Autowired
    protected DistributedLock distributedLock;

    @Autowired
    private RedisCacheService cacheService;

    @Autowired
    private VrTokenManager vrTokenManager;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
       syncNonce(sysCredentials.getAddress());
    }

    public Object submitLoseTx(String address) throws Exception {
        String key = getNonceKey(address);
        BigInteger remote = getRemoteNonce(address);
        long rtV = remote.longValue() - 1;
        if(cacheService.get(key,Long.class) <= rtV) return Response.success("不需要修复");
        VRToken vrToken = VRToken.load(vrTokenManager.getLastVrToken().getContractAddress(),admin,new RawTransactionManager(admin,sysCredentials),web3Properties.getGasPrice(),web3Properties.getGasLimit());
        TransactionReceipt receipt = vrToken.mint(sysCredentials.getAddress(), UnitConvertUtils.toWei(new BigDecimal("1")),UUID.randomUUID().toString()).send();
        return receipt;
    }

    protected BigInteger getRemoteNonce(String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = admin.ethGetTransactionCount(
                address, DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    public String getNonceKey(String address){
        return "eth:nonce:" + MD5Encoder.md5(web3Properties.getNonceKeyPrefix()) + ":" + address;
    }

    public synchronized BigInteger getNonce(String address) {
        String key = getNonceKey(address);
        long nonce = cacheService.increment(key);
        return new BigInteger(String.valueOf(nonce));
//        return new BigInteger("0");
    }

    public void syncNonce(String address) {
        String key = getNonceKey(address);
        String requestId = UUID.randomUUID().toString();
        boolean lock = false;
        try{
            lock = distributedLock.tryLock(key, requestId, 60);
            if(lock){
                BigInteger remote = getRemoteNonce(address);
                long rtV = remote.longValue() - 1;
                if(!cacheService.exists(key)) {
                    cacheService.put(key, rtV);
                } else {
                    if(cacheService.get(key,Long.class) < rtV) {
                        cacheService.put(key, rtV);
                    }
                }
                logger.info("init nonce value from block value {}",rtV);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } finally {
            if(lock) distributedLock.unLock(key,requestId);
        }

    }
}
