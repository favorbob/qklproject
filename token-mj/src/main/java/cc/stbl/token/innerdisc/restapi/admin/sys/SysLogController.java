package cc.stbl.token.innerdisc.restapi.admin.sys;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.modules.sys.entity.SysLog;
import cc.stbl.token.innerdisc.modules.sys.service.SysLogService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.ks.common.datastore.model.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = PathPrefix.ADMIN_PATH + "/sys/sysLog")
//@RequiresRoles({Auth.ROLE_ADMIN})
@Api(description = "系统/操作日志管理")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;


    //新增日志
    @RequestMapping(value = {"/addLog"},method = RequestMethod.POST)
    @ApiOperation("日志添加")
    public Response<String> addSysLog(@RequestBody @Valid SysLogProto.AddLogRequest request){

        SysLog sysLog = new SysLog();
        sysLog.setIpAddress(request.getIpAddress());
        sysLog.setUserId(request.getUserId());
        sysLog.setUserName(request.getUserName());
        sysLog.setIsSysLog(request.getIsSysLog());
        sysLog.setLogInfo(request.getLogInfo());
        sysLog.setUserType(request.getUserType());

        sysLogService.addSysLog(sysLog);    //添加日志
        return Response.success("新增日志成功");
    }

    @RequestMapping(value = {"/getSysLogInfo"},method = RequestMethod.POST)
    @ApiOperation("获取日志信息")
    public Response<Pager<SysLog>> getSysLogInfo(@RequestBody @Valid SysLogProto.querySysLogInfoRequest request){
        SysLog sysLog = new SysLog();
        sysLog.setUserId(request.getUserId());
        sysLog.setIpAddress(request.getIpAddress());
        sysLog.setStartDate(request.getStartDate());
        sysLog.setEndDate(request.getEndDate());
        sysLog.setUserType(request.getUserType());
        int pageno = request.getPageNo();
        int pagesize = request.getPageSize();

        return Response.success(sysLogService.getSysLogInfoPage(sysLog, pageno, pagesize ));
    }


}
