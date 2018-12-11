package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.remote.RemoteFileManager;
import cc.stbl.token.innerdisc.modules.appversion.entity.MallAppVersion;
import cc.stbl.token.innerdisc.modules.appversion.service.MallAppVersionService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App版本Controller
 * @author LinJJ
 * @version 2018-04-23
 */

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/appversion/mallAppVersion")
public class AppVersionController {

	@Autowired
	private RemoteFileManager remoteFileManager;
	@Autowired
	private MallAppVersionService mallAppVersionService;

	
	@ModelAttribute
	public MallAppVersion get(@RequestParam(required=false) String id) {
		MallAppVersion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallAppVersionService.get(id);
		}
		if (entity == null){
			entity = new MallAppVersion();
		}
		return entity;
	}
	
	//@RequiresPermissions("appversion:mallAppVersion:view")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Response<Pager<MallAppVersion>> list(@RequestBody AppVersionProto.QueryCondition condition) {
        MallAppVersion mallAppVersion = new MallAppVersion();
        BeanUtils.copyProperties(condition, mallAppVersion);
        Pager<MallAppVersion> page = mallAppVersionService.findPage(mallAppVersion, condition.getPageNo(), condition.getPageSize());
        return Response.success(page);
	}

	//@RequiresPermissions("appversion:mallAppVersion:view")
	@RequestMapping(value = "form", method = RequestMethod.GET)
	public Response<MallAppVersion> form(@RequestParam("id")String id) {
        MallAppVersion mallAppVersion = mallAppVersionService.getById(id);
        return Response.success(mallAppVersion);
    }

	//@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Response save(@RequestBody @Valid AppVersionProto.RequestSave requestSave) {
		MallAppVersion mallAppVersion = new MallAppVersion();
		BeanUtils.copyProperties(requestSave,mallAppVersion);
		mallAppVersionService.saveOrUpdate(mallAppVersion);
		return Response.success();
	}
	
	//@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Response delete(@RequestBody AppVersionProto.Detele detele) {
		mallAppVersionService.delete(detele.getId());
		return Response.success();
	}

	//@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "batchDelete", method = RequestMethod.POST)
	public Response batchDelete(@RequestParam("ids")String[] ids) {
		mallAppVersionService.deleteByIds(ids);
        return Response.success();
	}

	//@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "nextVersionCode", method = RequestMethod.GET)
	public Response<Integer> nextVersionCode(@RequestParam("clientType") String clientType) {
		Integer nextVersionCode = this.mallAppVersionService.nextVersionCode(clientType);
		return Response.success(nextVersionCode);
	}
}
