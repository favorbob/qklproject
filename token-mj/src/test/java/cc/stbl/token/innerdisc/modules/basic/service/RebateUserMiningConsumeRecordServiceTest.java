package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.modules.basic.entity.RebateUserMiningConsumeRecord;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import com.ks.common.datastore.model.Pager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class RebateUserMiningConsumeRecordServiceTest extends AbstractTestCase {

    @Autowired
    private RebateUserMiningConsumeRecordService rebateUserMiningConsumeRecordService;
    @Autowired
    private VrUserInfoService vrUserInfoService;

    @Test
    public void updateUserMiningRecord() {
        VrUserInfo vrUserInfo = new VrUserInfo();
        List<VrUserInfo> userInfoList = vrUserInfoService.findList(vrUserInfo, 10);
        for (int i = 0; i < 5; i++){
            for (VrUserInfo user : userInfoList){
                String userId = user.getUserId();
                BigDecimal amount = new BigDecimal(100);
                rebateUserMiningConsumeRecordService.updateUserMiningRecord(userId, amount);
            }
        }
        RebateUserMiningConsumeRecord rebateUserMiningConsumeRecord = new RebateUserMiningConsumeRecord();
        Long count = rebateUserMiningConsumeRecordService.findCount(rebateUserMiningConsumeRecord);
        Long pageSize = 10L;
        Long pageNo = count/pageSize;
        Pager<RebateUserMiningConsumeRecord> page = rebateUserMiningConsumeRecordService.findPage(rebateUserMiningConsumeRecord, pageNo.intValue(), pageSize.intValue());
        List<RebateUserMiningConsumeRecord> list = page.getList();
        for (RebateUserMiningConsumeRecord record : list){
            System.out.println("RebateUserMiningConsumeRecordServiceTest.updateUserMiningRecord ---> " + record);
        }
    }

}