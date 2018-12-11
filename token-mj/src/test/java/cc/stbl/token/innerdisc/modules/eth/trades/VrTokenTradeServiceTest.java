package cc.stbl.token.innerdisc.modules.eth.trades;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.enums.BEnum;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.tx.EthNonceManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.modules.payment.service.MyAssetsService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;

public class VrTokenTradeServiceTest extends AbstractTestCase {

    @Autowired
    private VrTokenTradeService vrTokenTradeService;

    @Autowired
    private EthWalletService walletService;

    @Autowired
    private MyAssetsService myAssetsService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private Credentials sysCredentials;

    @Autowired
    private EthNonceManager ethNonceManager;

    @Test
    public void testCharge() throws Exception {
        mockAppLogin("leon");
        System.out.println(vrTokenManager.balanceOf(sysCredentials.getAddress()));
        for (int i = 0 ;i< 5; i ++) {
            boolean applySuccess = vrTokenTradeService.chargeAsset(ShiroUtils.getLoginUserId(), BEnum.SYS_CHARGE,new BigDecimal("10"),"系统充值");
            System.out.println(applySuccess);
        }
        Thread.sleep(10000L);
        dump();
    }

    @Test
    public void testUpgrade() throws Exception {
        System.out.println(vrTokenTradeService.importDataFrom("0xba0369d5a63a41279fd9fb4778bbd1a8a5a4c278","leon:1"));
    }

    @Test
    public void rest() throws Exception {
        mockAppLogin("leon");
        vrTokenTradeService.restAiic(ShiroUtils.getLoginUserId(),new BigDecimal("100"),"abc");
        Thread.sleep(10000L);
        dump();
        vrTokenTradeService.restAsset(ShiroUtils.getLoginUserId(),new BigDecimal("120"),"abc");
        Thread.sleep(10000L);
        dump();
        vrTokenTradeService.restMj(ShiroUtils.getLoginUserId(),new BigDecimal("300"),"abc");
        Thread.sleep(10000L);
        dump();
        vrTokenTradeService.restAll(ShiroUtils.getLoginUserId(),new BigDecimal("1000"),new BigDecimal("2000"),new BigDecimal("3000"),"abc");
        Thread.sleep(10000L);
        dump();
    }

    @Test
    public void testRestAsset() throws Exception {
        mockAppLogin("leon");
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(ShiroUtils.getLoginUserId());
        dump();
        TransactionReceipt receipt = vrToken.resetAsset(wallet.getAddress(),
                UnitConvertUtils.toWei(new BigDecimal("1000")),UnitConvertUtils.toWei(new BigDecimal("2000")),
                UnitConvertUtils.toWei(new BigDecimal("3000")),"abc")
                  .send();
        System.out.println(JSON.toJSONString(receipt));
        dump();
    }

    @Test
    public void testActivity() throws InterruptedException {
        mockAppLogin("leon");
        boolean applySuccess = vrTokenTradeService.activeUser(ShiroUtils.getLoginUserId(),new BigDecimal("300"),new BigInteger("6"),"测试测试");
        System.out.println(applySuccess);
        Thread.sleep(30000L);
        dump();
    }

    @Test
    public void testTransferTest() throws InterruptedException {
        mockAppLogin("leon");
        vrTokenTradeService.transferFromExt(BEnum.TRANSFER,"qwertyui","6e3f97c0e7f740448eb6d10d99bbdfa6","a00ae70c37b44a83b361dce8b0413108",new BigDecimal(100),"测试","测试","tradeNo");
        Thread.sleep(30000L);

    }

    @Test
    public void returns() throws Exception {
        vrTokenTradeService.integralRebateByAmplify("5bbfec4a29f44fa1b11a4954da0df293");
        Thread.sleep(30000L);
    }


    @Test
    public void name() throws Exception {
        mockAppLogin("18934790727");
        vrTokenTradeService.integralAmplifyOf(ShiroUtils.getLoginUserId(),"123456",new BigDecimal(1),2);
        Thread.sleep(30000L);
    }

    private void dump() {
        System.out.println(JSON.toJSONString(myAssetsService.myAssetsHome()));
    }
}
