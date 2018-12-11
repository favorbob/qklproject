/**
* generator by mybatis plugin Thu Aug 23 14:57:29 CST 2018
**/
package cc.stbl.token.innerdisc.modules.basic.service;

import cc.stbl.token.innerdisc.common.TradeNoGenerator;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.dao.VrUserDeviceMapper;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseNumDTO;
import cc.stbl.token.innerdisc.modules.basic.dto.DeviceUseTimeDTO;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserDevice;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserInfo;
import cc.stbl.token.innerdisc.restapi.app.user.UserDeviceProto;
import com.ks.common.datastore.model.Pager;
import com.ks.common.datastore.service.DataStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VrUserDeviceService extends DataStoreServiceImpl<String, VrUserDevice, VrUserDeviceMapper> {

    @Autowired
    private TradeNoGenerator tradeNoGenerator;
    @Autowired
    private ResourceUserUsedTimeService resourceUserUsedTimeService;

    /**
     * 新增设备
     * @param code 编码（唯一标识）
     * @param name 设备名称
     */
    public void addDevice(String code,String name){
        Date date = new Date();
        VrUserDevice vrUserDevice = new VrUserDevice();
        String id = tradeNoGenerator.get();
        vrUserDevice.setId(id);
        vrUserDevice.setCode(code);
        vrUserDevice.setName(name);
        vrUserDevice.setBindState(1);    //默认绑定
        vrUserDevice.setBindTime(date);
        vrUserDevice.setCreateDate(date);
        String userId = ShiroUtils.getLoginUserId();
        vrUserDevice.setUserId(userId);
        vrUserDevice.setState(1);    //默认可用
        mapper.insertSelective(vrUserDevice);
    }


    /**
     * 删除设备（软删除）
     * @param deviceId 设备ID
     */
    public void delDevice(String deviceId){
        mapper.delDevice(deviceId);
    }


    /**
     * 获取用户设备列表
     * @param userId 用户ID
     * @return
     */
    public Pager<VrUserDevice> getUserDeviceList(String userId,Integer pageNo,Integer pageSize){
        //        List<VrUserDevice> deviceList = mapper.getByUserId(userId);
//        return deviceList;
        VrUserDevice vrUserDevice = new VrUserDevice();
        vrUserDevice.setUserId(userId);
        vrUserDevice.setState(1);   //数据状态正常的才搜出来
        Pager<VrUserDevice> pager = super.findPage(vrUserDevice, pageNo, pageSize);
        return pager;
    }


    /**
     * 获取设备使用时长记录
     * @param deviceId 设备ID
     * @param month 月份
     * @param resourceType 资源类型 1游戏，2电影
     * @param pageNo 页码
     * @param pageSize 每页记录数
     * @return
     */
    public UserDeviceProto.DeviceUseTimeRecordResp getDeviceUseTimeRecord(String deviceId,Date month,Integer resourceType,Integer pageNo,Integer pageSize){
        Long countTimes = resourceUserUsedTimeService.getDeviceUseTimesInfo(deviceId,null);
        Long movieTimes = resourceUserUsedTimeService.getDeviceUseTimesInfo(deviceId,2);
        Long gameTimes = resourceUserUsedTimeService.getDeviceUseTimesInfo(deviceId,1);
        Pager<DeviceUseTimeDTO> pager = resourceUserUsedTimeService.getDeviceUseTimeRecord(deviceId,month,resourceType,pageNo,pageSize);
        UserDeviceProto.DeviceUseTimeRecordResp resp = new UserDeviceProto.DeviceUseTimeRecordResp();
        resp.setCountTimes(countTimes);
        resp.setMovieTimes(movieTimes);
        resp.setGameTimes(gameTimes);
        resp.setPager(pager);
        return resp;
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
    public UserDeviceProto.DeviceUseNumRecordResp getDeviceUseNumRecord(String deviceId,Date month,Integer resourceType,Integer pageNo,Integer pageSize){
        Integer countNum = resourceUserUsedTimeService.getDeviceUseNumInfo(deviceId,null);
        Integer movieNum = resourceUserUsedTimeService.getDeviceUseNumInfo(deviceId,2);
        Integer gameNum = resourceUserUsedTimeService.getDeviceUseNumInfo(deviceId,1);
        Pager<DeviceUseNumDTO> pager = resourceUserUsedTimeService.getDeviceUseNumRecord(deviceId,month,resourceType,pageNo,pageSize);
        UserDeviceProto.DeviceUseNumRecordResp resp = new UserDeviceProto.DeviceUseNumRecordResp();
        resp.setCountNum(countNum);
        resp.setMovieNum(movieNum);
        resp.setGameNum(gameNum);
        resp.setPager(pager);
        return resp;
    }


}