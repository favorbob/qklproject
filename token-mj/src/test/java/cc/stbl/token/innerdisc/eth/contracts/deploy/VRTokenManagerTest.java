package cc.stbl.token.innerdisc.eth.contracts.deploy;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class VRTokenManagerTest extends AbstractTestCase {

    @Autowired
    private VrTokenManager vrTokenManager;

    @Test
    public void testDeploy() throws Exception {
        mockAdminLogin("leon");
        VRToken vrToken = vrTokenManager.deployed(ShiroUtils.getLoginUserId(),"XX币","XXC",new BigInteger("1000000000"));
        System.out.println(vrToken.getContractAddress());
    }

//    @Test
    public void getVrToken() throws Exception {
        VRToken vrToken = vrTokenManager.getLastVrToken();
        System.out.println(UnitConvertUtils.toEther(vrToken.initTotalSupply().send()));
    }
}
