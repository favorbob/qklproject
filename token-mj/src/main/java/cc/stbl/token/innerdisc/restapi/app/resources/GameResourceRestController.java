package cc.stbl.token.innerdisc.restapi.app.resources;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.service.ResourceGameService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = PathPrefix.API_PATH + "/resource/games")
@Api(tags = {"resource"},description = "资源-游戏")
public class GameResourceRestController {

    @Autowired
    private ResourceGameService resourceGameService;

    @RequestMapping(value = "/gameList",method = RequestMethod.POST)
    @ApiOperation("游戏列表 天宇")
    public Response<List<GameResourceProto.ResponseGameDetail>> gameList(@RequestBody @Valid GameResourceProto.RequestGameList request){
        return Response.success(resourceGameService.getPageList(request));
    }

    @RequestMapping(value = "/gameDetail",method = RequestMethod.POST)
    @ApiOperation("游戏详情 天宇")
    public Response<GameResourceProto.ResponseGameDetail> gameDetail(@RequestParam("id")String id){
        return Response.success(resourceGameService.getGameDetailToShow(id));
    }
}
