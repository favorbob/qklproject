package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.JavaUUIDGenerator;
import cc.stbl.token.innerdisc.modules.sys.entity.SysProperties;
import cc.stbl.token.innerdisc.modules.sys.service.SysPropertiesService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 后台参数管理Controller
 * @author caojinbo
 * @version 2018-09-27
 */

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/sysProperties")
@Api(description = "系统/参数管理")
public class SysPropertiesController {

	@Autowired
	private SysPropertiesService sysPropertiesService;

	//新增参数
	@RequestMapping(value = {"/addProperties"},method = RequestMethod.POST)
	@ApiOperation("新增参数")
	public Response<String> addProperties(@RequestBody @Valid SysPropertiesProto.AddPropsRequest request){

		SysProperties sysProperties = new SysProperties();
		BeanUtils.copyProperties(request, sysProperties);

		sysProperties.setId(JavaUUIDGenerator.get());
		sysProperties.setCreateDate(new Date());

		sysPropertiesService.add(sysProperties);
		return Response.success("新增参数成功");
	}

	//更新参数
	@RequestMapping(value = {"/updateProperties"},method = RequestMethod.POST)
	@ApiOperation("更新参数")
	public Response<String> updateProperties(@RequestBody @Valid SysPropertiesProto.PropsRequest request){

		SysProperties sysProperties = new SysProperties();
		BeanUtils.copyProperties(request, sysProperties);

		sysPropertiesService.update(sysProperties);	//更新
		return Response.success("更新参数成功");
	}

	//根据ID 获得参数对象
	@RequestMapping(value = {"/getProperties"},method = RequestMethod.POST)
	@ApiOperation("获取单个参数对象")
	public Response<SysProperties> get(@RequestBody SysPropertiesProto.Select select) {
		SysProperties entity = null;
		if (StringUtils.isNotBlank(select.getId())){
			entity = sysPropertiesService.get(select.getId());
		}
		if (entity == null){
			entity = new SysProperties();
		}
		return Response.success(entity);
	}

	//根据分页的参数对象
	@RequestMapping(value = {"/getPageListProperties"},method = RequestMethod.POST)
	@ApiOperation("获取参数对象list")
	public Response<Pager<SysProperties>> list(@RequestBody SysPropertiesProto.PropsRequestList condition) {
		SysProperties sysProps = new SysProperties();
		BeanUtils.copyProperties(condition, sysProps);
		Pager<SysProperties> page = sysPropertiesService.findPage(sysProps, condition.getPageNo(), condition.getPageSize());
		return Response.success(page);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ApiOperation("删除参数")
	public Response delete(@RequestBody SysPropertiesProto.Detele detele) {
		sysPropertiesService.delete(detele.getId());
		return Response.success();
	}

	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	@ApiOperation("批量删除参数")
	public Response batchDelete(@RequestBody SysPropertiesProto.Ids ids) {
		String[] deids = ids.getIds().split(",");
		sysPropertiesService.deleteByIds(deids);
		return Response.success();
	}

}
