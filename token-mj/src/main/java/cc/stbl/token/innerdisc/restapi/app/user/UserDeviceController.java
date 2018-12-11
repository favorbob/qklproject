package cc.stbl.token.innerdisc.restapi.app.user;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.entity.VrUserDevice;
import cc.stbl.token.innerdisc.modules.basic.service.VrUserDeviceService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/device")
@Api(description = "用户设备模块 @yangpeng")
public class UserDeviceController {

    @Autowired
    private VrUserDeviceService vrUserDeviceService;


    @RequestMapping(method = RequestMethod.POST,value = "/addDevice")
    @ApiOperation("新增设备")
    public Response addDevice(@RequestBody @Valid UserDeviceProto.addDeviceReq addDeviceReq){
        vrUserDeviceService.addDevice(addDeviceReq.getCode(),addDeviceReq.getName());
        return Response.success();
    }


    @RequestMapping(method = RequestMethod.POST,value = "/delDevice")
    @ApiOperation("删除设备")
    public Response delDevice(@RequestBody UserDeviceProto.delDeviceReq delDeviceReq){
        vrUserDeviceService.delDevice(delDeviceReq.getDeviceId());
        return Response.success();
    }


    @RequestMapping(method = RequestMethod.POST,value = "/getUserDeviceList")
    @ApiOperation("获取用户设备列表")
    public Response<Pager<VrUserDevice>> getUserDeviceList(@RequestBody UserDeviceProto.getUserDeviceListReq getUserDeviceListReq){
        String userId = ShiroUtils.getLoginUserId();
        Pager<VrUserDevice> devicePager = vrUserDeviceService.getUserDeviceList(userId,getUserDeviceListReq.getPageNo(),getUserDeviceListReq.getPageSize());
//        List<VrUserEquipment> equipmentList = vrUserEquipmentService.getUserEquList(userId);
//        UserEquipmentProto.UserEquListResp userEquListResp = new UserEquipmentProto.UserEquListResp();
//        userEquListResp.setEquipmentList(equipmentList);
        return Response.success(devicePager);
    }


    @RequestMapping(method = RequestMethod.POST,value = "/getDeviceUseTimeRecord")
    @ApiOperation("获取设备使用时长记录")
    public Response<UserDeviceProto.DeviceUseTimeRecordResp> getDeviceUseTimeRecord(@RequestBody UserDeviceProto.DeviceUseTimeRecordReq deviceUseTimeRecordReq){
        String deviceId = deviceUseTimeRecordReq.getDeviceId();
        Date month = deviceUseTimeRecordReq.getMonth();
        Integer resourceType = deviceUseTimeRecordReq.getResourceType();
        Integer pageNo = deviceUseTimeRecordReq.getPageNo();
        Integer pageSize = deviceUseTimeRecordReq.getPageSize();
        UserDeviceProto.DeviceUseTimeRecordResp deviceUseTimeRecordResp = vrUserDeviceService.getDeviceUseTimeRecord(deviceId,month,resourceType,pageNo,pageSize);
        return Response.success(deviceUseTimeRecordResp);
    }


    @RequestMapping(method = RequestMethod.POST,value = "/getDeviceUseNumRecord")
    @ApiOperation("获取设备使用次数记录")
    public Response<UserDeviceProto.DeviceUseNumRecordResp> getDeviceUseNumRecord(@RequestBody UserDeviceProto.DeviceUseNumRecordReq deviceUseNumRecordReq){
        String deviceId = deviceUseNumRecordReq.getDeviceId();
        Date month = deviceUseNumRecordReq.getMonth();
        Integer resourceType = deviceUseNumRecordReq.getResourceType();
        Integer pageNo = deviceUseNumRecordReq.getPageNo();
        Integer pageSize = deviceUseNumRecordReq.getPageSize();
        UserDeviceProto.DeviceUseNumRecordResp deviceUseNumRecordResp = vrUserDeviceService.getDeviceUseNumRecord(deviceId,month,resourceType,pageNo,pageSize);
        return Response.success(deviceUseNumRecordResp);
    }

}
