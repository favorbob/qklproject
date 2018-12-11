package cc.stbl.token.innerdisc.common.filter;

import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class CorsFilterConfiguration {

    @Bean
    public FilterRegistrationBean corsFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CORSFilter());
        Map<String,String> params = new HashMap<>();
        params.put("cors.allowOrigin","*");
        params.put("cors.supportedMethods","GET, POST, HEAD, PUT, DELETE");
        params.put("cors.supportedHeaders","Accept, Origin, X-Requested-With, Content-Type, Last-Modified,token");
//        params.put("cors.supportedHeaders","*");
        params.put("cors.exposedHeaders","Set-Cookie");
        params.put("cors.supportsCredentials","true");
        bean.setInitParameters(params);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("/*");
        return bean;
    }
}
