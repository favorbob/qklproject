/**
* generator by mybatis plugin Wed Aug 22 18:10:59 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.dao.ResourceUserUsedNumberMapper;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceUserUsedNumber;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ResourceUserUsedNumberService extends DataStoreServiceImpl<String, ResourceUserUsedNumber, ResourceUserUsedNumberMapper> {

    @Async
    public void saveOrUpdateNumber(String userId, String deviceId, String resourceId, Integer resourceType){
        ResourceUserUsedNumber usedNumber = new ResourceUserUsedNumber();
        usedNumber.setDeviceId(deviceId);
        usedNumber.setResourceId(resourceId);
        usedNumber.setUserId(userId);
        ResourceUserUsedNumber one = super.findOne(usedNumber);
        if (one != null){
            Integer number = one.getUsedNumber();
            one.setUsedNumber(number + 1);
            super.update(one);
        }else {
            String usedNumberId = JavaUUIDGenerator.get();
            usedNumber.setId(usedNumberId);
            usedNumber.setUsedNumber(1);
            usedNumber.setResourceType(resourceType);
            super.add(usedNumber);
        }
    }
}