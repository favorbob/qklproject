package cc.stbl.token.innerdisc.modules.eth.trades;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.common.enums.MEnum;
import cc.stbl.token.innerdisc.eth.contracts.VRToken;
import cc.stbl.token.innerdisc.eth.contracts.deploy.VrTokenManager;
import cc.stbl.token.innerdisc.eth.util.UnitConvertUtils;
import cc.stbl.token.innerdisc.modules.eth.entity.EthMintRecord;
import cc.stbl.token.innerdisc.modules.eth.entity.EthWallet;
import cc.stbl.token.innerdisc.modules.eth.service.EthMintRecordService;
import cc.stbl.token.innerdisc.modules.eth.service.EthWalletService;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class VrTokenMintService {

    @Autowired
    private EthMintRecordService mintRecordService;

    @Autowired
    private VrTokenManager vrTokenManager;

    @Autowired
    private EthWalletService walletService;

    // 返利
    public boolean mint(String userId, BigDecimal mintAmount, MEnum mEnum, String remark){
        VRToken vrToken = vrTokenManager.getLastVrToken();
        EthWallet wallet = walletService.getUserWallet(userId);
        if(!vrTokenManager.canMint(vrToken)) throw new StructWithCodeException(ResponseCode.MINT_NOT_CAN);
        EthMintRecord mintRecord = new EthMintRecord();
        mintRecord.setAddress(wallet.getAddress());
        mintRecord.setMintAmount(mintAmount);
        mintRecord.setId(JavaUUIDGenerator.get());
        mintRecord.setMintDate(new Date());
        mintRecord.setMintType(mEnum.code);
        mintRecord.setMintStatus(TradeConsts.TRADE_STATUS_PROCESSING);
        mintRecord.setUserId(userId);
        mintRecord.setRemark(remark);
        mintRecord.setLastBlockNumber(vrTokenManager.getLastBlockNumber());
        mintRecordService.add(mintRecord);
        vrToken.mint(wallet.getAddress(),UnitConvertUtils.toWei(mintAmount),mintRecord.getId()).sendAsync();
        return true;
    }
}
