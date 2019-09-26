package com.alan.shiro.test.web.base.config;

import com.alan.shiro.test.comm.Global;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;


public class PasswordHashedCredentials extends HashedCredentialsMatcher {

    public PasswordHashedCredentials(){
        this.setHashAlgorithmName(Global.ENCRYPTION);
        this.setHashIterations(Global.HASHITERATIONS);
        this.setStoredCredentialsHexEncoded(true);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return super.doCredentialsMatch(token, info);
    }
}
