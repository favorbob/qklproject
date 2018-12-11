package cc.stbl.token.innerdisc.common.shiro.authc;

import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordEncoder {

    private final String algorithmName;
    private final int hashIterations;

    public PasswordEncoder(String algorithmName, int hashIterations) {
        this.algorithmName = algorithmName;
        this.hashIterations = hashIterations;
    }

    public String encoder(String plainPassword,String salt){
        SimpleHash simpleHash = new SimpleHash(algorithmName,plainPassword,salt);
        simpleHash.setIterations(hashIterations);
        return simpleHash.toString();
    }
}
