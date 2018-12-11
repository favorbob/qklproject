package cc.stbl.token.innerdisc.restapi.admin.base;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.basic.entity.ActivateCardFlow;
import cc.stbl.token.innerdisc.modules.basic.service.ActivateCardFlowService;
import cc.stbl.token.innerdisc.modules.sys.entity.SysProperties;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * 后台参数管理Controller
 * @author caojinbo
 * @version 2018-09-27
 */

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/base/activatecard")
@Api(description = "财务管理/激活卡异动查询")
public class ActivateCardFlowController {

	@Autowired
	private ActivateCardFlowService activateCardFlowService;

	//跟进分页 获取异动查询信息
	@RequestMapping(value = {"/getPageList"},method = RequestMethod.POST)
	@ApiOperation("获取参数对象list")
	public Response<Pager<ActivateCardFlow>> getPageList(@RequestBody ActivateCardFlowProto.RequestQuery condition) {
		ActivateCardFlow cardFlow = new ActivateCardFlow();
		BeanUtils.copyProperties(condition, cardFlow);
		Pager<ActivateCardFlow> page = activateCardFlowService.getActivateCardPage(cardFlow, condition.getPageNo(), condition.getPageSize());
		return Response.success(page);
	}

}
