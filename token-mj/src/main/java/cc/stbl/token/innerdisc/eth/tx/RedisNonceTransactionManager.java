package cc.stbl.token.innerdisc.eth.tx;

import org.apache.regexp.RE;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author leon
 * @Date 2018/10/15
 */
public class RedisNonceTransactionManager extends RawTransactionManager{

    private EthNonceManager ethNonceManager;

    private Credentials credentials;
    private Web3j web3j;

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials,EthNonceManager ethNonceManager) {
        super(web3j, credentials, ChainId.NONE);
        this.ethNonceManager = ethNonceManager;
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials, byte chainId) {
        super(web3j, credentials, chainId);
    }

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials, byte chainId, TransactionReceiptProcessor transactionReceiptProcessor) {
        super(web3j, credentials, chainId, transactionReceiptProcessor);
    }

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials, byte chainId, int attempts, long sleepDuration) {
        super(web3j, credentials, chainId, attempts, sleepDuration);
    }

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials) {
        super(web3j, credentials);
    }

    public RedisNonceTransactionManager(Web3j web3j, Credentials credentials, int attempts, int sleepDuration) {
        super(web3j, credentials, attempts, sleepDuration);
    }

    @Override
    protected BigInteger getNonce() throws IOException {
//        return super.getNonce();
//        return new BigInteger("1");
        return ethNonceManager.getNonce(credentials.getAddress());
    }

    public RedisNonceTransactionManager getUserNonceTransactionManager(Credentials credentials){
        return new RedisNonceTransactionManager(this.web3j,credentials,this.ethNonceManager);
    }
}
