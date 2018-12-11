package cc.stbl.token.innerdisc.eth.contracts.deploy;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.eth.contracts.DataStore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;

public class DataStoreManagerTest extends AbstractTestCase {

    @Autowired
    private DataStoreManager dataStoreManager;

    @Autowired
    private Credentials sysCredentials;

    @Test
    public void deployed() throws Exception {
       DataStore dataStore = dataStoreManager.deployed("leon");
       dataStore.setUint256(sysCredentials.getAddress(),new BigInteger("100000000")).send();
       System.out.println(dataStore.getUint256(sysCredentials.getAddress()));
    }

//    @Test
    public void testQuery() throws Exception {
        DataStore dataStore = dataStoreManager.getLastDispatcher();
        System.out.println(dataStore.getUint256(sysCredentials.getAddress()).send());
    }
}
