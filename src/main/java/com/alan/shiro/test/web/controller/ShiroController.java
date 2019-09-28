package com.alan.shiro.test.web.controller;

import com.alan.shiro.test.entity.ShUser;
import com.alan.shiro.test.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ShiroController {

    @Autowired
    UserService userService;

    @RequestMapping("/admin/success")
    public String success(){
        return "权限通过";
    }

    @RequestMapping("/nonauth")
    public String nonauth(){
        return "无权限";
    }

    @RequestMapping("/login")
    public String login(String username,String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject sub =  SecurityUtils.getSubject();
        try {
            sub.login(token);
        } catch (UnknownAccountException e) {
            return "暂无该用户";
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            return "密码错误";
        }
        ShUser u = userService.getUser(username);
        sub.getSession().setAttribute(u.getUsername(),u);
        return "this is login page";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "退出成功";
    }

    @RequestMapping("/registerUser")
    public String registerUser(ShUser user){
        return userService.registerUser(user).toString();
    }

    @RequestMapping("/member/test")
    public String registerUser(){
        return "ok";
    }


    /**
     *     @RequiresRoles需拥有 admin 角色
     *     @RequiresGuest  游客
     *     @RequiresAuthentication 当前Subject已经通过login进行身份验证;即 Subjec.isAuthenticated()返回
     *     @RequiresUser 表示当前Subject已经身份验证或者通过记住我登录的
     *     @RequiresPermissions()  哪个权限
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @RequestMapping("/member/testRole")
    public String testRole(){
        return "ok";
    }


}
