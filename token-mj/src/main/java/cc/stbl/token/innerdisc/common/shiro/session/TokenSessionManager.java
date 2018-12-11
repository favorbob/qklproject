package cc.stbl.token.innerdisc.common.shiro.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class TokenSessionManager extends DefaultWebSessionManager {

    private static final String ACCESS_TOKEN_IN_HEADER = "token";
    private static final String TOKEN_SESSION_ID_SOURCE = "create_by_access_token";

    private static final Logger logger = LoggerFactory.getLogger(TokenSessionManager.class);

    private static final Long maxIdleTimeInMillis = 7L * 24L * 60L * 60L * 1000L;

    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(ACCESS_TOKEN_IN_HEADER);
        if(StringUtils.isNotBlank(token)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,TOKEN_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        }
        return super.getSessionId(request,response);
    }

    @Override
    protected Session newSessionInstance(SessionContext context) {
        Session session = super.newSessionInstance(context);
        session.setTimeout(getGlobalSessionTimeout());
        if(context instanceof DefaultWebSessionContext){
            ServletRequest request = ((DefaultWebSessionContext) context).getServletRequest();
            String token = WebUtils.toHttp(request).getHeader(ACCESS_TOKEN_IN_HEADER);
            if(token != null){
                session.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,TOKEN_SESSION_ID_SOURCE);
                session.setTimeout(maxIdleTimeInMillis); //1个月
            }
        }
        return session;
    }

    @Override
    protected void applyGlobalSessionTimeout(Session session) {
        session.setTimeout(getGlobalSessionTimeout());
        if(TOKEN_SESSION_ID_SOURCE.equals(session.getAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE))) {
            session.setTimeout(maxIdleTimeInMillis); //1个月
        }
        logger.debug("set session={}`timeout={}",session.getId(),session.getTimeout());
        onChange(session);
    }
}
