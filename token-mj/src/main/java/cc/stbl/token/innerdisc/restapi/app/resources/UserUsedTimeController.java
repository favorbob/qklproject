package cc.stbl.token.innerdisc.restapi.app.resources;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceUserUsedTimeService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/resource/userUsedTime")
@Api(tags = {"resource"},description = "用户使用时长")
public class UserUsedTimeController {

    @Autowired
    private ResourceUserUsedTimeService service;

    @RequestMapping(value = "/countUsedTimeStart",method = RequestMethod.POST)
    @ApiOperation("统计用户时间开始 天宇")
    public Response<UserUsedTimeProto.ResponseCountUsedTimeStart> countUsedTimeStart(@RequestBody @Valid UserUsedTimeProto.RequestCountUsedTimeStart start){
        String id = service.countUsedTimeStart(start, ShiroUtils.getLoginUserId());
        UserUsedTimeProto.ResponseCountUsedTimeStart responseCountUsedTimeStart = new UserUsedTimeProto.ResponseCountUsedTimeStart();
        responseCountUsedTimeStart.setId(id);
        return Response.success(responseCountUsedTimeStart);
    }

    @RequestMapping(value = "/continuousCall",method = RequestMethod.POST)
    @ApiOperation("持续呼叫统计时间 天宇")
    public Response continuousCall(@RequestBody @Valid UserUsedTimeProto.RequestContinuousCall continuousCall){
        service.continuousCall(continuousCall);
        return Response.success();
    }

}
