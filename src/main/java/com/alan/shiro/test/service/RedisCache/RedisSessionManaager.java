package com.alan.shiro.test.service.RedisCache;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

public class RedisSessionManaager extends DefaultWebSessionManager {
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable id = getSessionId(sessionKey);
        ServletRequest request =null;
        if(sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request!=null&&id!=null){
            Session session =  (Session) request.getAttribute(id.toString());
            if(session!=null){
                return  session;
            }
        }
        Session session =  super.retrieveSession(sessionKey);
        if(request!=null&&id!=null){
            request.setAttribute(id.toString(),session);
        }
        return session;
    }
}
