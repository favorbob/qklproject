package cc.stbl.token.innerdisc.eth.wallet;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

public class WalletUtilTest {

    @Test
    public void name() throws Exception {
//        WalletUtils.loadCredentials()
        String json =  EthWalletUtils.generateWalletFileJson("123456");
        System.out.println(json);
    }

    @Test
    public void updatePassword() throws Exception {
        String source = "jsonstr"; // eth_wallet.wallet_file
        Credentials credentials = EthWalletUtils.loadCredentials("oldPassword",source);
        String newKeyStore = EthWalletUtils.updatePassword("newPassword",credentials);
        System.out.println(newKeyStore);
        //update eth_wallet.wallet_file
    }
}
