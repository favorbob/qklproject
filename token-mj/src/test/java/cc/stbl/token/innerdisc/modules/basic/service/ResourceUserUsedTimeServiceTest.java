package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.common.enums.ResourceTypeEnum;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceUserUsedNumber;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceUserUsedTime;
import cc.stbl.token.innerdisc.restapi.app.resources.UserUsedTimeProto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ResourceUserUsedTimeServiceTest extends AbstractTestCase {

    @Autowired
    private ResourceUserUsedTimeService userUsedTimeService;
    @Autowired
    private ResourceUserUsedNumberService usedNumberService;

    @Test
    public void countUsedTimeStart() {
        try {
            UserUsedTimeProto.RequestCountUsedTimeStart usedTimeStart = new UserUsedTimeProto.RequestCountUsedTimeStart();
            usedTimeStart.setDeviceId("testDeviceId");
            usedTimeStart.setResourceId("testResourceId");
            usedTimeStart.setResourceType(ResourceTypeEnum.GAME.getCode());
            String id = userUsedTimeService.countUsedTimeStart(usedTimeStart, "testUserId");

            UserUsedTimeProto.RequestContinuousCall continuousCall = new UserUsedTimeProto.RequestContinuousCall();
            continuousCall.setId(id);
            continuousCall.setEnd("0");
            for (int i = 0; i < 10; i++){
                System.err.println("ResourceUserUsedTimeServiceTest.countUsedTimeStart --->" + i);
                userUsedTimeService.continuousCall(continuousCall);
                Thread.sleep(5000L);
            }
            continuousCall.setEnd("1");
            userUsedTimeService.continuousCall(continuousCall);
            /*Thread.sleep(5000L);
            userUsedTimeService.continuousCall(continuousCall);*/

            ResourceUserUsedTime userUsedTime = userUsedTimeService.get(id);
            ResourceUserUsedNumber usedNumber = new ResourceUserUsedNumber();
            usedNumber.setDeviceId(userUsedTime.getDeviceId());
            usedNumber.setResourceId(userUsedTime.getResourceId());
            usedNumber.setUserId(userUsedTime.getUserId());
            ResourceUserUsedNumber one = usedNumberService.findOne(usedNumber);
            System.out.println("ResourceUserUsedTimeServiceTest.countUsedTimeStart --->" + userUsedTime);
            System.out.println("ResourceUserUsedTimeServiceTest.countUsedTimeStart --->" + one);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void updateContinuousTime() {
    }

    @Test
    public void countUsedTimeEnd() {
    }
}