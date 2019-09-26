package com.alan.shiro.test.web.controller;

import com.alan.shiro.test.entity.ShUser;
import com.alan.shiro.test.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "thi is login page";
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
}
