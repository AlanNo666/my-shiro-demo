package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.service.RedisCache.RedisCacheManager;
import com.alan.shiro.test.service.RedisCache.RedisSessionDAO;
import com.alan.shiro.test.service.RedisCache.RedisSessionManaager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean
    public DefaultWebSecurityManager defaultSecurityManager(){
        DefaultWebSecurityManager manager =  new DefaultWebSecurityManager();
        manager.setRealm(myRealm());
        manager.setSessionManager(mySessionManager());
        manager.setCacheManager(redisCacheManager());
        return manager;
    }

    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm =new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        myRealm.setAuthorizationCachingEnabled(true);
        myRealm.setCachingEnabled(true);
        myRealm.setAuthenticationCachingEnabled(true);
        return myRealm;
    }

    @Bean
    public RedisCacheManager redisCacheManager(){
        return new RedisCacheManager();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(defaultSecurityManager());
        filter.setLoginUrl("/login");
        filter.setUnauthorizedUrl("/nonauth");
        filter.setSuccessUrl("/admin/success");
        Map<String,String> linkedMap = new LinkedHashMap<>();
        linkedMap.put("/test/*","anon");
        linkedMap.put("/admin/*","roles[admin]");
        linkedMap.put("/member/*","authc");
        filter.setFilterChainDefinitionMap(linkedMap);
        return filter;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        return new PasswordHashedCredentials();
    }

    @Bean
    public RedisSessionManaager  mySessionManager(){
        RedisSessionManaager  sessionManager = new RedisSessionManaager ();
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(){
       RedisSessionDAO redisDao =  new RedisSessionDAO();
        return redisDao;
    }







}
