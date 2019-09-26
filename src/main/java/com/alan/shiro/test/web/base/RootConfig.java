package com.alan.shiro.test.web.base;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.FilterType.REGEX;

@Configuration
@ComponentScan(basePackages = {"com.alan.shiro.test"},
        excludeFilters = {
            @ComponentScan.Filter(type = REGEX,pattern ="com.alan.shiro.test.web.controller")
        }
)
public class RootConfig {
}
