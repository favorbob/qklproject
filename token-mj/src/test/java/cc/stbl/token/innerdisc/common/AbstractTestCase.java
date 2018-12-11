package cc.stbl.token.innerdisc.common;

import cc.stbl.token.innerdisc.TokenBootStrap;
import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import cc.stbl.token.innerdisc.common.shiro.service.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = {TokenBootStrap.class, AbstractTestCase.TestConfiguration.class},webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class AbstractTestCase extends AbstractJUnit4SpringContextTests{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Configuration
    public static class TestConfiguration {
        @Bean
        public SecurityManager securityManager(SessionManager sessionManager, CacheManager cacheManager, AuthorizingRealm realm){
            DefaultSecurityManager manager = new DefaultSecurityManager();
            manager.setSessionManager(sessionManager);
            manager.setCacheManager(cacheManager);
            manager.setRealm(realm);
            return manager;
        }
    }

    @Before
    final public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        SecurityManager securityManger = wac.getBean(SecurityManager.class);
        ThreadContext.bind(securityManger);
        contextInit();
    }

    protected void contextInit(){}

    protected void mockAdminLogin(String userName) {
        UserPwdToken token = new UserPwdToken();
        token.setUsername(userName);
        token.setPassword("123456".toCharArray());
        token.setUserType(LoginUser.UserType.ADMIN_MOCK);
        SecurityUtils.getSubject().login(token);
    }

    protected void mockAppLogin(){mockAppLogin("leon");}
    protected void mockAppLogin(String username){
        UserPwdToken token = new UserPwdToken();
        token.setUsername(username);
        token.setPassword("123456".toCharArray());
        token.setUserType(LoginUser.UserType.APP_MOCK);
        SecurityUtils.getSubject().login(token);
    }
}

/**
 *
 * HelloWorld
 Java代码  收藏代码
 @Test
 public void testView() throws Exception {
 MvcResult result = paymentMvc.perform(MockMvcRequestBuilders.get("/user/1"))
 .andExpect(MockMvcResultMatchers.view().name("user/view"))
 .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
 .andDo(MockMvcResultHandlers.print())
 .andReturn();
 Assert.assertNotNull(result.getModelAndView().getModel().get("user"));

 }
 1、paymentMvc.perform执行一个请求；
 2、MockMvcRequestBuilders.get("/user/1")构造一个请求
 3、ResultActions.andExpect添加执行完成后的断言
 4、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
 5、ResultActions.andReturn表示执行完成后返回相应的结果。
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
