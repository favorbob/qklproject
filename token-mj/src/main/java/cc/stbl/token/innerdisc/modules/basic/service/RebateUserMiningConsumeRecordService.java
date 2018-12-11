/**
* generator by mybatis plugin Mon Aug 20 18:00:12 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.enums.MEnum;
import cc.stbl.token.innerdisc.modules.basic.dao.RebateUserMiningConsumeRecordMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserMiningConsumeRecord;
import cc.stbl.token.innerdisc.modules.eth.trades.VrTokenMintService;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class RebateUserMiningConsumeRecordService extends DataStoreServiceImpl<String, RebateUserMiningConsumeRecord, RebateUserMiningConsumeRecordMapper> {

    @Autowired
    private VrTokenMintService vrTokenMintService;
    @Autowired
    private SysPropertiesService sysPropertiesService;

    public void updateUserMiningRecord(String userId, BigDecimal amount){
        RebateUserMiningConsumeRecord rebateUserMiningConsumeRecord = new RebateUserMiningConsumeRecord();
        rebateUserMiningConsumeRecord.setUserId(userId);
        RebateUserMiningConsumeRecord record = super.findOne(rebateUserMiningConsumeRecord);
        Date date = new Date();
        BigDecimal miningAmountSum;
        // 记录累计消费挖矿金额记录
        if (record == null){
            record = rebateUserMiningConsumeRecord;
            record.setId(UUID.randomUUID().toString());
            record.setLastMiningTime(date);
            miningAmountSum = amount;
            record.setMiningAmountSum(miningAmountSum);
            record.setConsumedAmountSum(BigDecimal.ZERO);
            super.add(record);
        }else {
            record.setLastMiningTime(date);
            BigDecimal miningAmountSumBefore = record.getMiningAmountSum();
            miningAmountSum = miningAmountSumBefore.add(amount);
            record.setMiningAmountSum(miningAmountSum);
            super.update(record);
        }
        // 执行挖矿
        vrTokenMintService.mint(userId, amount,MEnum.CONSUME_MINT, "消费挖矿");
        // 核销已用挖矿金额
        sysPropertiesService.setProperties("MintRebatePoint", this.getClass().getName(), "500");
        BigDecimal rebatePoint = sysPropertiesService.getBigDecimal("MintRebatePoint", this.getClass().getName());
        if (record.getMiningAmountSum().compareTo(rebatePoint) >= 0){
            // 挖矿金额达到返利点，执行置零操作，同时累计已置零金额
            BigDecimal[] divideAndRemainder = miningAmountSum.divideAndRemainder(rebatePoint);
            BigDecimal remainder = divideAndRemainder[1];
            record.setMiningAmountSum(remainder); // 余数
            BigDecimal consumedAmountSum = record.getConsumedAmountSum();
            if (remainder.compareTo(BigDecimal.ZERO) == 0){
                consumedAmountSum = consumedAmountSum.add(miningAmountSum);
            }else {
                consumedAmountSum = consumedAmountSum.add(miningAmountSum.divide(remainder));
            }
            record.setConsumedAmountSum(consumedAmountSum);
            record.setLastSumTime(date);
            super.update(record);
        }
    }

}