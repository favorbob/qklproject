package cc.stbl.token.innerdisc.restapi.app.resources;


import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceVideoService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/resource/video")
@Api(tags = {"resource"},description = "资源-电影")
public class VideoResourceRestController {

    @Autowired
    private ResourceVideoService resourceVideoService;

    @RequestMapping(value = "/videoList",method = RequestMethod.POST)
    @ApiOperation("电影列表 天宇")
    public Response<List<VideoResourceProto.ResponseVideoDetail>> gameList(@RequestBody @Valid VideoResourceProto.RequestVideoList request){
        return Response.success(resourceVideoService.getPageList(request));
    }

    @RequestMapping(value = "/videoDetail",method = RequestMethod.POST)
    @ApiOperation("电影详情 天宇")
    public Response<VideoResourceProto.ResponseVideoDetail> videoDetail(@RequestParam("id")String id){
        return Response.success(resourceVideoService.getVideoDetailToShow(id));
    }
}
