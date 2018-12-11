package cc.stbl.token.innerdisc.eth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigInteger;

@ConfigurationProperties(prefix = "ethereum.web3")
public class Web3Properties {

    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private String httpProvider;
    private String nonceKeyPrefix;

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getHttpProvider() {
        return httpProvider;
    }

    public void setHttpProvider(String httpProvider) {
        this.httpProvider = httpProvider;
    }

    public String getNonceKeyPrefix() {
        return nonceKeyPrefix;
    }

    public void setNonceKeyPrefix(String nonceKeyPrefix) {
        this.nonceKeyPrefix = nonceKeyPrefix;
    }
}
