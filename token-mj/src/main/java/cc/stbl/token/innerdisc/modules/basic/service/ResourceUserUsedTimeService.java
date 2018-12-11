/**
* generator by mybatis plugin Wed Aug 22 18:10:59 GMT+08:00 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.dao.ResourceUserUsedTimeMapper;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseNumDTO;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseTimeDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.ResourceUserUsedTime;
import com.ks.common.datastore.model.Pager;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import cc.stbl.token.innerdisc.restapi.app.resources.UserUsedTimeProto;
import com.ks.common.datastore.exception.StructWithCodeException;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.Date;


@Service
public class ResourceUserUsedTimeService extends DataStoreServiceImpl<String, ResourceUserUsedTime, ResourceUserUsedTimeMapper> {



    /**
     * 获取设备使用时长记录
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @param pageNo 页码
     * @param pageSize 每页记录数
     * @return
     */
    public Pager<DeviceUseTimeDTO> getDeviceUseTimeRecord(String deviceId,Date month,Integer resourceType,Integer pageNo,Integer pageSize){
        Long total = mapper.getDeviceUseTimeRecordCount(deviceId,month,resourceType);
        Integer offset = (pageNo - 1) * pageSize;
        if (total == 0L) {
            return new Pager(pageNo, pageSize, new ArrayList(), total);
        } else {
            RowBounds rowBounds = new RowBounds(offset,pageSize);
            List<DeviceUseTimeDTO> list = mapper.getDeviceUseTimeRecord(deviceId,month,resourceType,rowBounds);
            return new Pager(pageNo, pageSize, list, total);
        }
    }


    /**
     * 获取设备使用次数记录
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型
     * @param pageNo 页码
     * @param pageSize 每页记录数
     * @return
     */
    public Pager<DeviceUseNumDTO> getDeviceUseNumRecord(String deviceId, Date month, Integer resourceType, Integer pageNo, Integer pageSize){
        Long total = mapper.getDeviceUseNumRecordCount(deviceId,month,resourceType);
        Integer offset = (pageNo - 1) * pageSize;
        if (total == 0L) {
            return new Pager(pageNo, pageSize, new ArrayList(), total);
        } else {
            RowBounds rowBounds = new RowBounds(offset,pageSize);
            List<DeviceUseNumDTO> list = mapper.getDeviceUseNumRecord(deviceId,month,resourceType,rowBounds);
            return new Pager(pageNo, pageSize, list, total);
        }

    }

    /**
     * 获取设备使用时长信息
     * @param deviceId 设备ID
     * @param resourceType 资源类型
     * @return
     */
    public Long getDeviceUseTimesInfo(String deviceId,Integer resourceType){
        return mapper.getDeviceUseTimes(deviceId,resourceType);
    }


    /**
     * 获取设备使用次数信息
     * @param deviceId 设备ID
     * @param resourceType 资源类型
     * @return
     */
    public Integer getDeviceUseNumInfo(String deviceId,Integer resourceType){
        return mapper.getDeviceUseNum(deviceId,resourceType);
    }



    private static Logger logger = LoggerFactory.getLogger(ResourceUserUsedTimeService.class);

    public final String TIME_KEY = "USER_USED_TIME:";
    public final String TIME_ZSET_KEY = "USER_USED_TIME_ZSET";
    public final String TIME_DATA_KEY = "USER_DATA_TIME:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceUserUsedNumberService usedNumberService;

    public String countUsedTimeStart(UserUsedTimeProto.RequestCountUsedTimeStart start, String userId){
        String id = JavaUUIDGenerator.get();
        Date date = new Date();
        long startTimeMillis = date.getTime();
        // 写缓存，定时任务轮询redis，超过5秒没更新的视为异常结束
        stringRedisTemplate.opsForValue().set(TIME_KEY + id, String.valueOf(startTimeMillis));
        stringRedisTemplate.opsForZSet().add(TIME_ZSET_KEY, id, startTimeMillis);
        // 写缓存，结束时插入数据库
        ResourceUserUsedTime userUsedTime = new ResourceUserUsedTime();
        userUsedTime.setId(id);
        userUsedTime.setDeviceId(start.getDeviceId());
        userUsedTime.setResourceId(start.getResourceId());
        userUsedTime.setUserId(userId);
        userUsedTime.setResourceType(start.getResourceType());
        userUsedTime.setStartTime(date);
        redisTemplate.opsForValue().set(TIME_DATA_KEY + id, userUsedTime);

        return id;
    }

    public void continuousCall(UserUsedTimeProto.RequestContinuousCall continuousCall){
        this.updateContinuousTime(continuousCall.getId());
        boolean isEnd = UserUsedTimeProto.RequestContinuousCall.isEnd(continuousCall.getEnd());
        if (isEnd){
            this.countUsedTimeEnd(continuousCall.getId());
        }
    }

    public void updateContinuousTime(String id){
        String timeKey = TIME_KEY + id;
        String timeMillis = stringRedisTemplate.opsForValue().get(timeKey);
        if (timeMillis == null){
            throw new StructWithCodeException(ResponseCode.USER_USED_TIME_ERROR.code);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long intervalTime = currentTimeMillis - Long.valueOf(timeMillis);
        if (intervalTime <= 5000){
            Long increment = stringRedisTemplate.opsForValue().increment(timeKey, 5000L);
            stringRedisTemplate.opsForZSet().add(TIME_ZSET_KEY, id, increment);
        }else {
            throw new StructWithCodeException(ResponseCode.USER_USED_TIME_ERROR.code);
        }

    }

    @Async
    public void countUsedTimeEnd(String id){
        logger.info("countUsedTimeEnd.id:" + id);
        String timeKey = TIME_KEY + id;
        String timeMillis = stringRedisTemplate.opsForValue().get(timeKey);
        stringRedisTemplate.opsForValue().getOperations().delete(timeKey);
        stringRedisTemplate.opsForZSet().remove(TIME_ZSET_KEY, id);

        ResourceUserUsedTime userUsedTime = (ResourceUserUsedTime)redisTemplate.opsForValue().get(TIME_DATA_KEY + id);
        redisTemplate.opsForValue().getOperations().delete(TIME_DATA_KEY + id);
        Date startTime = userUsedTime.getStartTime();
        long times = Long.valueOf(timeMillis) - startTime.getTime();
        userUsedTime.setEndTime(new Date(Long.valueOf(timeMillis)));
        userUsedTime.setTimes(times);
        super.add(userUsedTime);

        usedNumberService.saveOrUpdateNumber(userUsedTime.getUserId(), userUsedTime.getDeviceId(), userUsedTime.getResourceId(), userUsedTime.getResourceType());

    }


}