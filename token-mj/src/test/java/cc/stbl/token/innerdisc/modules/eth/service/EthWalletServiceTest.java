package cc.stbl.token.innerdisc.modules.eth.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.eth.wallet.EthWalletUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;

public class EthWalletServiceTest extends AbstractTestCase {

    @Autowired
    private EthWalletService walletService;

    @Test
    public void getUserWallets() throws Exception {
        EthWallet wallet = walletService.getUserWallet("5bbfec4a29f44fa1b11a4954da0df293");
        Credentials credentials = EthWalletUtils.loadCredentials("qwertyui",wallet.getWalletFile());
        System.out.println(JSON.toJSONString(credentials));
    }
    @Test
    public void updatePassword() throws Exception {
        walletService.updatePasswordByUserId("5bbfec4a29f44fa1b11a4954da0df293","qwertyui","123456");
    }
}
