package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.service.RedisCache.RedisCacheManager;
import com.alan.shiro.test.service.RedisCache.RedisManager;
import com.alan.shiro.test.service.props.RedisProps;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean
    public DefaultSecurityManager defaultSecurityManager(){
        DefaultSecurityManager manager =  new DefaultWebSecurityManager();
        manager.setRealm(myRealm());
        return manager;
    }

    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm =new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        myRealm.setCachingEnabled(true);
        myRealm.setAuthenticationCachingEnabled(true);
        myRealm.setCacheManager(redisCacheManager());
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
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        return new PasswordHashedCredentials();
    }






}
