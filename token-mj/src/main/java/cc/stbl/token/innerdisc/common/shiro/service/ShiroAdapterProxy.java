package cc.stbl.token.innerdisc.common.shiro.service;

import cc.stbl.token.innerdisc.common.shiro.authc.UserPwdToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ShiroAdapterProxy implements InitializingBean{

    private Map<String,ShiroAdapter> shiroAdapterMap;

    @Autowired
    private ApplicationContext applicationContext;

    public LoginUser findLoginUser(UserPwdToken userPwdToken) {
        return shiroAdapterMap.get(userPwdToken.getUserType().name()).findLoginUser(userPwdToken.getUsername());
    }

    public UserRolePermission findByUser(String userId,LoginUser.UserType userType) {
        return shiroAdapterMap.get(userType.name()).findByUser(userId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        shiroAdapterMap = new HashMap<>();
        Map<String,ShiroAdapter> adapterMaps = applicationContext.getBeansOfType(ShiroAdapter.class);
        for (ShiroAdapter shiroAdapter : adapterMaps.values()) {
            String name = shiroAdapter.listerUserType().name();
            if(shiroAdapterMap.containsKey(name)) throw new RuntimeException("重复的ShiroAdapter["+ name +"]");
            shiroAdapterMap.put(name,shiroAdapter);
        }
    }
}
