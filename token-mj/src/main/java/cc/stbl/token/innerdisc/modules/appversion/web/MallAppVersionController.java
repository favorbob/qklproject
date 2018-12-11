/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *//*

package cc.stbl.token.innerdisc.modules.appversion.web;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.t9.common.file.remote.RemoteFileManager;
import team.t9.mall.modules.appversion.entity.MallAppVersion;
import team.t9.mall.modules.appversion.service.MallAppVersionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

*/
/**
 * App版本Controller
 * @author LinJJ
 * @version 2018-04-23
 *//*

@Controller
@RequestMapping(value = "${adminPath}/appversion/mallAppVersion")
public class MallAppVersionController extends BaseController {

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
	
	@RequiresPermissions("appversion:mallAppVersion:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallAppVersion mallAppVersion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallAppVersion> page = mallAppVersionService.findPage(new Page<MallAppVersion>(request, response), mallAppVersion); 
		model.addAttribute("page", page);
		return "mall/modules/appversion/mallAppVersionList";
	}

	@RequiresPermissions("appversion:mallAppVersion:view")
	@RequestMapping(value = "form")
	public String form(MallAppVersion mallAppVersion, Model model) {
		model.addAttribute("mallAppVersion", mallAppVersion);
		return "mall/modules/appversion/mallAppVersionForm";
	}

	@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "save")
	public String save(MallAppVersion mallAppVersion, Model model, RedirectAttributes redirectAttributes) {
		boolean isNewRecord = mallAppVersion.getIsNewRecord();
		if (!beanValidator(model, mallAppVersion)){
			return form(mallAppVersion, model);
		}
		if (StringUtils.isNotBlank(mallAppVersion.getUrl())) {
			mallAppVersion.setSize(remoteFileManager.getFile(mallAppVersion.getUrl()).getLength());
		}
		try{
            mallAppVersionService.save(mallAppVersion);
        }catch (Exception e){
			if (e instanceof DuplicateKeyException) {
				if (((DuplicateKeyException)e).getCause() instanceof MySQLIntegrityConstraintViolationException) {
					addMessage(model, "版本号重复，请检查。");
					if (isNewRecord) {
						mallAppVersion.setId(null);
					}
					return this.form(mallAppVersion, model);
				}
			}
			throw e;
        }
		addMessage(redirectAttributes, "保存App版本成功");
		return "redirect:"+Global.getAdminPath()+"/appversion/mallAppVersion/?repage";
	}
	
	@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "delete")
	public String delete(MallAppVersion mallAppVersion, RedirectAttributes redirectAttributes) {
		mallAppVersionService.delete(mallAppVersion);
		addMessage(redirectAttributes, "删除App版本成功");
		return "redirect:"+Global.getAdminPath()+"/appversion/mallAppVersion/?repage";
	}

	@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "batchDelete")
	public String batchDelete(@RequestParam("ids")String[] ids, RedirectAttributes redirectAttributes) {
		mallAppVersionService.batchDelete(ids);
		addMessage(redirectAttributes, "删除App版本成功");
		return "redirect:"+Global.getAdminPath()+"/appversion/mallAppVersion/?repage";
	}

	@RequiresPermissions("appversion:mallAppVersion:edit")
	@RequestMapping(value = "nextVersionCode")
	@ResponseBody
	public Object nextVersionCode(String clientType) throws IOException {
		long nextVersionCode = this.mallAppVersionService.nextVersionCode(clientType);
		return nextVersionCode;
	}
}*/
