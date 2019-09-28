package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.service.RedisCache.RedisCacheManager;
import com.alan.shiro.test.service.RedisCache.RedisSessionDAO;
import com.alan.shiro.test.service.RedisCache.RedisSessionManaager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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

    /**
     * Shiro生命周期处理器
     * @return
     */
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


    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(defaultSecurityManager());
        return sourceAdvisor;
    }









}
