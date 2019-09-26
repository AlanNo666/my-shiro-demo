package com.alan.shiro.test.web.base;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
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
}
