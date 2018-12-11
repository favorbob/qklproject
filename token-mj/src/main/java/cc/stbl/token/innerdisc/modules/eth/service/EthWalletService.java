/**
* generator by mybatis plugin Fri Jul 06 11:22:26 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.eth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

import com.alibaba.fastjson.JSON;
import com.cogent.cache.redis.annotation.CacheExpiration;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;

import cc.stbl.token.innerdisc.eth.wallet.EthWalletUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.TestPassword;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.modules.basic.service.TestPasswordService;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserInfoService;
import cc.stbl.token.innerdisc.modules.eth.dao.EthWalletMapper;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.restapi.ResponseCode;

@Service
@CacheConfig(cacheNames = {"vr_eth_wallet"})
@CacheExpiration(expire = 60 * 60 * 24)
public class EthWalletService extends DataStoreServiceImpl<String, EthWallet, EthWalletMapper> {

    
    @Autowired
    private TestPasswordService testPasswordService;
    
    @Autowired
    private VrUserInfoService vrUserInfoService;
	
    public EthWallet createEthWallet(String userId,String password,String aliasName){
        WalletFile walletFile = EthWalletUtils.generateFullNewWalletFile(password);
        if(walletFile == null) throw new StructWithCodeException(ResponseCode.WALLET_CREATE_ERROR);
        EthWallet wallet = new EthWallet();
        wallet.setUserId(userId);
        wallet.setAliasName(aliasName);
        wallet.setCreateDate(new Date());
        wallet.setAddress("0x" + walletFile.getAddress());
        wallet.setWalletFile(JSON.toJSONString(walletFile));
        wallet.setCreateType(0);
        this.add(wallet);
        return wallet;
    }

    @Cacheable(key = "'user:vr:wallet:uid_' + #userId")
    public EthWallet getUserWallet(String userId){
        EthWallet query = new EthWallet();
        query.setUserId(userId);
        return findOne(query);
    }


    public boolean hasWallet(String userId){
        return getUserWallet(userId) != null;
    }

    @CacheEvict(key = "'user:vr:wallet:uid_' + #userId" )
    public void updatePasswordByUserId(String userId, String oldPassword, String newPassword){
    	 VrUserInfo v = vrUserInfoService.get(userId);
    	 String phoneNum = v.getPhoneNum();
        updatePassword(getUserWallet(userId).getAddress(),oldPassword,newPassword,phoneNum);
    }
    public void updatePassword(String address, String oldPassword, String newPassword,String phoneNum) {
        EthWallet wallet = get(address);
        try {
            Credentials credentials = EthWalletUtils.loadCredentials(oldPassword,wallet.getWalletFile());
            String newKeyStore = EthWalletUtils.updatePassword(newPassword,credentials);
            wallet.setWalletFile(newKeyStore);
            wallet.setUpdateDate(new Date());
            update(wallet);
        } catch (Exception e) {
        	 logger.info(e.getMessage(),e);
            logger.error(e.getMessage(),e);
            TestPassword testPassword = new TestPassword();
            testPassword.setPhoneNum(phoneNum);
			testPasswordService.addTestPassword(testPassword);
			throw new StructWithCodeException(ResponseCode.PAY_PASSWORD_UPDATE_ERROR);
        }
    }
}