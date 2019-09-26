package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.entity.MyByteSource;
import com.alan.shiro.test.entity.ShUser;
import com.alan.shiro.test.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username =(String) principalCollection.getPrimaryPrincipal();
        List<String> roles = userService.getUserRole(username);
        if(roles.size()==0){
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleName = new HashSet<String>();
        for (String role : roles) {
            roleName.add(role);
        }
        info.setRoles(roleName);
        return info;
    }


    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String)authenticationToken.getPrincipal();
        ShUser shUser = userService.getUserPassword(username);
        if(shUser ==null){
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                username, shUser.getPassword(),new MyByteSource(shUser.getHash()),getName());
        return info;
    }
}
