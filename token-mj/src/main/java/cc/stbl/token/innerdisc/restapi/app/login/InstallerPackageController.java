package cc.stbl.token.innerdisc.restapi.app.login;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.mvn.UserAgentUtils;
import cc.stbl.token.innerdisc.common.remote.oss.AbstractOSSFileTransfer;

import cc.stbl.token.innerdisc.modules.appversion.entity.MallAppVersion;
import cc.stbl.token.innerdisc.modules.appversion.service.MallAppVersionService;
import cc.stbl.token.innerdisc.modules.appversion.vo.VersionVo;
import cc.stbl.token.innerdisc.modules.basic.service.SiteSettingService;
import cc.stbl.token.innerdisc.restapi.PathPrefix;
import com.stbl.payment.providerImpl.bizorder.bean.ClientType;
import eu.bitwalker.useragentutils.OperatingSystem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(PathPrefix.API_PATH + "/packageMgr")
@Api(description = "移动端安装包管理@LinJJ")
@Validated
public class InstallerPackageController {

    private static final String DICT_TYPE = "IPC_DOWNLOAD_URL";
    @Autowired
    private MallAppVersionService mallAppVersionService;
    @Autowired
    private AbstractOSSFileTransfer remoteFileTransfer;

    @Autowired
    private SiteSettingService siteSettingService;
//
//    @GetMapping("url")
//    @ApiOperation("查询app下载的url,根据请求头部的source自动返回相关平台url，返回值为url")
//    public String url(RequestArgs args) {
//        String source = args.getSource();
//        String url = DictUtils.getDictValue(source.toUpperCase(), DICT_TYPE, null);
//        return url;
//    }


    @GetMapping("/checkAppUpdate")
    @ApiOperation("检查app版本更新")
    public Response<VersionVo> checkAppUpdate(String clientType, @ApiParam("客户端的当前版本") @RequestParam Integer clientVersion) {
        ClientType ct = ClientType.valueOf(clientType.toUpperCase());
        VersionVo versionVo = mallAppVersionService.checkVersion(ct, clientVersion);
        if (versionVo != null && ct == ClientType.IOS) {
            versionVo.setDownloadUrl(siteSettingService.getIosDownLoadUrl());
        }
        return Response.success(versionVo);
    }

    @GetMapping("/download")
    @ApiOperation("统一下载入口")
    public void download(@ApiParam("app类型。IOS/ANDROID/...") @RequestParam(required = false) ClientType clientType, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (clientType == null) {
            OperatingSystem operatingSystem = UserAgentUtils.getUserAgent(request).getOperatingSystem().getGroup();
            if (operatingSystem == OperatingSystem.ANDROID) {
                clientType = ClientType.ANDROID;
            } else if (operatingSystem == OperatingSystem.IOS) {
                clientType = ClientType.IOS;
            }
        }
        String url = null;
        if (clientType == ClientType.IOS) {
//            url = DictUtils.getDictValue(clientType.getValue().toUpperCase(), DICT_TYPE, null);
            url = siteSettingService.getIosDownLoadUrl();
        } else if (clientType == ClientType.ANDROID) {
//            url = DictUtils.getDictValue(clientType.getValue().toUpperCase(), DICT_TYPE, null);
//            this.downloadNewestFromServer(clientType, request, response);
//            return;
//          MallAppVersion version = mallAppVersionService.getNewestVersion(ClientType.ANDROID);
//          if(version ==null)
              url = siteSettingService.getAndroidDownLoadUrl();
//          else url = version.getUrl();

        }
        if (url != null) {
            response.sendRedirect(url);
        }
    }

    private void downloadNewestFromServer(@ApiParam("app类型。IOS/ANDROID/...") @RequestParam(required = false) ClientType clientType, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url;MallAppVersion newestVersion = mallAppVersionService.getNewestVersion(clientType);
        url = newestVersion.getUrl();
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if(userAgent.contains("micromessenger")) {//微信客户端
            request.getRequestDispatcher("/WEB-INF/views/ipcTips.jsp").forward(request, response);
            return ;
        }
        try {
            response.addHeader("Content-Length", "" + newestVersion.getSize());
            response.addHeader("Content-Disposition", "attachment;filename=\"" + new String("睿购.apk".getBytes("UTF8"),"iso-8859-1")+"\"");
            remoteFileTransfer.download(null, url, response.getOutputStream());
        }finally {
            response.flushBuffer();
            return;
        }
    }


}
