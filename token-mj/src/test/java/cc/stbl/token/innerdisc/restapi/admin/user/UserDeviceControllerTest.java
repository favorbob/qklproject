package cc.stbl.token.innerdisc.restapi.admin.user;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.BaseProto;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import cc.stbl.token.innerdisc.restapi.admin.sys.SysUserProto;
import cc.stbl.token.innerdisc.restapi.app.user.UserDeviceProto;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Date;

public class UserDeviceControllerTest extends AbstractTestCase {

    @Test
    public void testAddDevice() throws Exception{
        mockAppLogin();
        UserDeviceProto.addDeviceReq addDeviceReq = new UserDeviceProto.addDeviceReq();
        addDeviceReq.setCode("testCode");
        addDeviceReq.setName("测试设备");
        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.API_PATH + "/device/addDevice")
                .contentType("application/json")
                .content(JSON.toJSONString(addDeviceReq)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testDelDevice() throws Exception{

        UserDeviceProto.delDeviceReq delDeviceReq = new UserDeviceProto.delDeviceReq();
        delDeviceReq.setDeviceId("210844245915996160");
        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.API_PATH + "/device/delDevice")
                .contentType("application/json")
                .content(JSON.toJSONString(delDeviceReq)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    public void testGetUserDeviceList() throws Exception{
        mockAppLogin();

        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.API_PATH + "/device/getUserDeviceList")
                .contentType("application/json")
                .content("{\"pageNo\" : 1,\"pageSize\" : 10}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testGetDeviceUseTimeRecord() throws Exception{
        mockAppLogin();
        UserDeviceProto.DeviceUseTimeRecordReq deviceUseTimeRecordReq = new UserDeviceProto.DeviceUseTimeRecordReq();
        deviceUseTimeRecordReq.setDeviceId("testDeviceId");
        deviceUseTimeRecordReq.setMonth(new Date());
        deviceUseTimeRecordReq.setResourceType(null);
        deviceUseTimeRecordReq.setPageNo(1);
        deviceUseTimeRecordReq.setPageSize(10);
        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.API_PATH + "/device/getDeviceUseTimeRecord")
                .contentType("application/json")
                .content(JSON.toJSONString(deviceUseTimeRecordReq)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    public void testGetDeviceUseNumRecord() throws Exception{
        mockAppLogin();
        UserDeviceProto.DeviceUseNumRecordReq deviceUseNumRecordReq = new UserDeviceProto.DeviceUseNumRecordReq();
        deviceUseNumRecordReq.setDeviceId("testDeviceId");
        deviceUseNumRecordReq.setMonth(new Date());
        deviceUseNumRecordReq.setResourceType(null);
        deviceUseNumRecordReq.setPageNo(1);
        deviceUseNumRecordReq.setPageSize(10);
        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.API_PATH + "/device/getDeviceUseNumRecord")
                .contentType("application/json")
                .content(JSON.toJSONString(deviceUseNumRecordReq)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
