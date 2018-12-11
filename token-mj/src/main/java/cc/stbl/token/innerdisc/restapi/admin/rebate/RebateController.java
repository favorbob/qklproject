package cc.stbl.token.innerdisc.restapi.admin.rebate;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/rebate")
@Api(description = "分销设置")
public class RebateController {

    @Autowired
    private SiteSettingService siteSettingService;

    @RequestMapping(value = {"/setInvolSetting"},method = RequestMethod.POST)
    @ApiOperation("内盘设置 天宇")
    public Response setInvolSetting(@RequestBody @Valid RebateProto.InvolSetting involSetting){
        siteSettingService.saveInvolSetting(involSetting);
        return Response.success();
    }

    @RequestMapping(value = {"/getInvolSetting"},method = RequestMethod.POST)
    @ApiOperation("获取内盘设置 天宇")
    public Response<RebateProto.InvolSetting> getInvolSetting(){
        RebateProto.InvolSetting involSetting = siteSettingService.getInvolSetting();
        return Response.success(involSetting);
    }
}
