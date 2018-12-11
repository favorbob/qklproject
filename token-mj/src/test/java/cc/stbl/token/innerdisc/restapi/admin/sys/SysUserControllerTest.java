package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


public class SysUserControllerTest extends AbstractTestCase {

    @Test
    public void testFindList() throws Exception {
        SysUserProto.ListRequest request = new SysUserProto.ListRequest();
        mockMvc.perform(MockMvcRequestBuilders.post(PathPrefix.ADMIN_PATH + "/sys/user/list")
                .contentType("application/json")
                .content(JSON.toJSONString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
