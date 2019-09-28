package com.alan.shiro.test.web.base;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;


@Configuration
@ComponentScan(
        basePackages = {"com.alan.shiro.test.web.controller"}
        )
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            super.configureMessageConverters(converters);
            converters.add(0,new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
            super.addCorsMappings(registry);
    }
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
            DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator =  new DefaultAdvisorAutoProxyCreator();
            advisorAutoProxyCreator.setProxyTargetClass(true);
            return advisorAutoProxyCreator;
    }





}
