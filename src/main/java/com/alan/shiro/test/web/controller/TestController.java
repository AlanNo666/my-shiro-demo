package com.alan.shiro.test.web.controller;

import com.alan.shiro.test.entity.ShUser;
import com.alan.shiro.test.service.UserService;
import com.alan.shiro.test.service.props.DbProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserService userService;
    @Autowired
    DbProp prop;

    @RequestMapping
    public String test() {
        System.out.println(prop.getUserName());
        return "mvc ok";
    }

    @RequestMapping("/getpass")
    public String getUser(){
        ShUser u = userService.getUserPassword("alan");
        return u.getPassword();
    }
}
