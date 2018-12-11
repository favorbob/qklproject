package cc.stbl.token.innerdisc.restapi.admin.resources;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceInfoService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/resource")
@Api(description = "应用管理")
public class ResourceInfoController {

    @Autowired
    private ResourceInfoService resourceInfoService;

    @RequestMapping(value = "/resourceList",method = RequestMethod.POST)
    @ApiOperation("应用列表 天宇")
    public Response<Pager<ResourceInfoProto.ResponseResourceDetail>> resourceList(@RequestBody @Valid ResourceInfoProto.RequestResourceInfoList requestResourceInfo){
        return Response.success(resourceInfoService.getPageList(requestResourceInfo));
    }

    @RequestMapping(value = "/resourceDetail",method = RequestMethod.POST)
    @ApiOperation("应用详情 天宇")
    public Response<ResourceInfoProto.ResponseResourceDetail> resourceDetail(@RequestParam("id")String id){
        return Response.success(resourceInfoService.getResourceDetailToShow(id));
    }
}
