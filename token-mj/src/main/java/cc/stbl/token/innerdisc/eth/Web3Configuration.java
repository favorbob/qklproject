package cc.stbl.token.innerdisc.eth;

import cc.stbl.token.innerdisc.eth.tx.EthNonceManager;
import cc.stbl.token.innerdisc.eth.tx.RedisNonceTransactionManager;
import cc.stbl.token.innerdisc.eth.wallet.EthWalletUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import rx.Observer;

import java.io.IOException;
import java.math.BigInteger;

@Configuration
@EnableConfigurationProperties({Web3Properties.class})
public class Web3Configuration {

    @Bean
    public Admin admin(Web3Properties properties){
        return Admin.build(new HttpService(properties.getHttpProvider()));
    }

    @Bean
    public Credentials sysCredentials(){
        String fileJson = "{\"address\":\"1db7cdab98810d1bbd835af8a431e93d07b352f6\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"6b2e175b17d5183c234e96b5d43fda0b\"},\"ciphertext\":\"54accf82b134382ff69b29054bcd1a23bf14a83cd39f41b2b26a0fe6dbd38e52\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"2fc09d162691667854d66d9a1d4c890f546b4c51b14741bf3a0272b7d31e50c6\"},\"mac\":\"9ef587531e943625bb82168a8fb5b202fa255ee18e2e8d35e80955eae4b7bf0d\"},\"id\":\"5aeed433-b05f-448f-921c-91f954974c20\",\"version\":3}";
        try {
            return  EthWalletUtils.loadCredentials("qwertyui@2018",fileJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public EthNonceManager ethNonceManager(){
        return new EthNonceManager();
    }


    public RedisNonceTransactionManager redisNonceTransactionManager(Credentials sysCredentials,Admin admin,EthNonceManager ethNonceManager){
        return new RedisNonceTransactionManager(admin,sysCredentials,ethNonceManager);
    }
}
