package cc.stbl.token.innerdisc.common.shiro.filter;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.response.ResponseUtils;
import cc.stbl.token.innerdisc.common.shiro.ShiroUtils;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ShiroAccessApiControlFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request,response);
        if(ShiroUtils.getLoginUserType() == null || ShiroUtils.getLoginUserType() != LoginUser.UserType.API) return false;
        return subject.isAuthenticated() || subject.isRemembered();
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request,response);
        Response rs = subject.isAuthenticated() ? Response.bulid(ResponseCode.OPT_NOAUTH_ERROR) : Response.bulid(ResponseCode.LOGIN_NO);
        ResponseUtils.responseJson((HttpServletResponse) response,rs);
        return false;
    }
}
