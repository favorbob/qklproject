package cc.stbl.token.innerdisc.restapi.admin.resources;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceThirdPartySettingsService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/resource/third")
@Api(description = "第三方应用")
public class ResourceThirdPartyController {

    @Autowired
    private ResourceThirdPartySettingsService resourceThirdPartySettingsService;

    @RequestMapping(value = "/addThirdPartySettings",method = RequestMethod.POST)
    @ApiOperation("保存第三方应用设置 天宇")
    public Response saveThirdPartySettings(@RequestBody @Valid ResourceThirdPartyProto.RequestThirdPartySettings thirdPartySettings){
        resourceThirdPartySettingsService.saveOrUpdate(thirdPartySettings);
        return Response.success();
    }

    @RequestMapping(value = "/getThirdPartySettings",method = RequestMethod.POST)
    @ApiOperation("查询第三方应用设置 天宇")
    public Response<ResourceThirdPartyProto.ResponseGetThirdPartySettings> getThirdPartySettings(@RequestBody @Valid ResourceThirdPartyProto.RequestGetThirdPartySettings settings){
        return Response.success(resourceThirdPartySettingsService.getThirdPartySettings(settings.getResourceType()));
    }
}
