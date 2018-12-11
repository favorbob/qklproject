package cc.stbl.token.innerdisc.restapi.admin.user;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ks.common.datastore.model.Pager;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.modules.basic.entity.VrAfterVoting;
import cc.stbl.token.innerdisc.modules.basic.service.VrAfterVotingService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/vrAfterVoting")
@Api(description = "兑换明细管理")
public class VrUserAfterVotingController {

    private static Logger logger = LoggerFactory.getLogger(VrUserAfterVotingController.class);

    @Autowired
    private VrAfterVotingService vrAfterVotingService;
 
 
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    @ApiOperation("转账列表")
    @ResponseBody
    public Response<Pager<VrAfterVoting>> list(@RequestBody @Valid VrAfterVotingProto.ListVrAfterVotingRequest request){
    	VrAfterVoting query = new VrAfterVoting();
        BeanUtils.copyProperties(request,query);
        Pager<VrAfterVoting> pager = vrAfterVotingService.findPage(query,request.getPageNo(),request.getPageSize());
        return Response.success(pager);
    }
}
