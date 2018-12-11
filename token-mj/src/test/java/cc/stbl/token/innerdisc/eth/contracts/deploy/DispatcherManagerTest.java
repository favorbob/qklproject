package cc.stbl.token.innerdisc.eth.contracts.deploy;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.eth.contracts.DataStore;
import cc.stbl.token.innerdisc.eth.contracts.Dispatcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;

public class DispatcherManagerTest extends AbstractTestCase{

    @Autowired
    private DispatcherManager dispatcherManager;

    @Autowired
    private DataStoreManager dataStoreManager;


    @Autowired
    private Credentials sysCredentials;

//    @Test
    public void testDeploy() throws Exception {
        DataStore dataStore = dataStoreManager.getLastDispatcher();
        Dispatcher dispatcher = dispatcherManager.deployed("test",dataStore.getContractAddress());
//        System.out.println(vrToken.initTotalSupply().send());
        System.out.println(dispatcher.getContractAddress());
    }

//    @Test
    public void testQuery() throws Exception {
        Dispatcher dispatcher = dispatcherManager.getLastDispatcher();
        System.out.println(dispatcher.isValid());
        System.out.println(dispatcher.getUint256(sysCredentials.getAddress()).send());
    }
}
