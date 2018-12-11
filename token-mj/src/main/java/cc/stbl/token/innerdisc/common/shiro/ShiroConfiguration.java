package cc.stbl.token.innerdisc.common.shiro;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.web.filter.DelegatingFilterProxy;

import cc.stbl.token.innerdisc.common.shiro.authc.PasswordEncoder;
import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import cc.stbl.token.innerdisc.common.shiro.filter.ShiroAccessAdminControlFilter;
import cc.stbl.token.innerdisc.common.shiro.filter.ShiroAccessApiControlFilter;
import cc.stbl.token.innerdisc.common.shiro.realm.SystemAuthorizingRealm;
import cc.stbl.token.innerdisc.common.shiro.redis.SpringRedisManager;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapter;
import cc.stbl.token.innerdisc.common.shiro.service.ShiroAdapterProxy;
import cc.stbl.token.innerdisc.common.shiro.service.UserRolePermission;
import cc.stbl.token.innerdisc.common.shiro.session.ShiroRedisSessionDAO;
import cc.stbl.token.innerdisc.common.shiro.session.TokenSessionManager;
import cc.stbl.token.innerdisc.restapi.PathPrefix;

@Configuration
public class ShiroConfiguration {

    private static final String CACHE_KEY = "shiro:cache:";
    private static final String SESSION_KEY = "shiro:session:";
	// private static final String NAME = "token";
	// private static final String VALUE = "/";

    public static final String PASSWORD_ALGORITHM_NAME = "SHA-256";
    public static final int PASSWORD_HASH_ITERATIONS = 1;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        Map<String,Filter> filters = new LinkedHashMap<>();
        filters.put("adminControl",new ShiroAccessAdminControlFilter());
        filters.put("apiControl",new ShiroAccessApiControlFilter());
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl  注意过滤器配置顺序 不能颠倒
//        filterChainDefinitionMap.put("/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put(PathPrefix.ADMIN_PATH +"/login", "anon");
        filterChainDefinitionMap.put(PathPrefix.ADMIN_PATH + "/**", "adminControl");

        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/login", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/user/forgotPasswordFirstStep", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/user/forgotPasswordSecondStep", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/regist", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/updateWalletPassword", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/getVerify", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/checkVerify", "anon");

        filterChainDefinitionMap.put(PathPrefix.API_PATH +"/user/retrievePassword", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH + "/sms/**", "anon");
        filterChainDefinitionMap.put(PathPrefix.API_PATH + "/packageMgr/**", "anon");
		filterChainDefinitionMap.put(PathPrefix.API_PATH + "/user/threeUsers", "user");
        filterChainDefinitionMap.put(PathPrefix.API_PATH + "/**", "apiControl");
        filterChainDefinitionMap.put("/**", "anon");

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
//        shiroFilterFactoryBean.setLoginUrl("/authFail");
        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean("delegatingFilterProxy")
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * Redis集群使用RedisClusterManager，单个Redis使用RedisManager
//     * @param redisProperties
     * @return
     */
    @Bean
    public IRedisManager redisManager(JedisConnectionFactory jedisFactory) throws IOException {
//        RedisClusterManager redisManager = new RedisClusterManager();
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(jedisFactory.getHostName() + ":" + jedisFactory.getPort());
//        redisManager.setPassword(jedisFactory.getPassword());
//        redisManager.setDatabase(jedisFactory.getDatabase());
        return new SpringRedisManager(jedisFactory);
    }
    @Bean
    public CacheManager cacheManager(IRedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
//        redisCacheManager.setExpire(86400);
        redisCacheManager.setKeyPrefix(CACHE_KEY);
        return redisCacheManager;
    }

    @Bean
    public SessionDAO sessionDAO(IRedisManager redisManager) {
        ShiroRedisSessionDAO redisSessionDAO = new ShiroRedisSessionDAO();
        redisSessionDAO.setKeyPrefix(SESSION_KEY);
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return redisSessionDAO;
    }

/*
    @Bean
    public CacheManager cacheManager(){
        MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
        return cacheManager;
    }*/
/*    @Bean
    public SessionDAO sessionDAO(){
        MemorySessionDAO sessionDAO = new MemorySessionDAO();
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }*/

    @Bean
    @ConditionalOnMissingBean(SecurityManager.class)
    public SecurityManager securityManager(SessionManager sessionManager,CacheManager cacheManager,AuthorizingRealm realm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setSessionManager(sessionManager);
        manager.setCacheManager(cacheManager);
        manager.setRealm(realm);
        return manager;
    }

    @Bean
	public SessionManager sessionManager(SessionDAO sessionDAO) {
        TokenSessionManager sessionManager = new TokenSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionIdCookieEnabled(false);
		// sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }

	/*
	 * @Bean public SimpleCookie simpleCookie() { SimpleCookie simpleCookie =
	 * new SimpleCookie(); simpleCookie.setName(NAME);
	 * simpleCookie.setValue(VALUE); simpleCookie.setMaxAge(3600); return
	 * simpleCookie; }
	 */



    @Bean
    public CredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(PASSWORD_ALGORITHM_NAME);//散列算法:这里使用MD5算法;HashAlgorithm
        hashedCredentialsMatcher.setHashIterations(PASSWORD_HASH_ITERATIONS);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public AuthorizingRealm authorizingRealm(CredentialsMatcher credentialsMatcher){
        SystemAuthorizingRealm realm = new SystemAuthorizingRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能 * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new PasswordEncoder(PASSWORD_ALGORITHM_NAME,PASSWORD_HASH_ITERATIONS);
    }

    @Bean
    @ConditionalOnMissingBean(ShiroAdapter.class)
    public ShiroAdapterProxy shiroAdapter(PasswordEncoder passwordEncoder){
        return new ShiroAdapterProxy() {
            @Override
            public LoginUser findLoginUser(UserPwdToken userPwdToken) {
                LoginUser user = new LoginUser();
                user.setPassword(passwordEncoder.encoder("123456","salt"));
                user.setSalt("salt");
                user.setUsername(userPwdToken.getUsername());
                user.setUserId(UUID.randomUUID().toString());
                user.setUserType(userPwdToken.getUserType());
                return user;
            }
            @Override
            public UserRolePermission findByUser(String userId, LoginUser.UserType userType) {
                return new UserRolePermission(null,null);
            }
        };
    }
}
