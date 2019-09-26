package com.alan.shiro.test.service.RedisCache;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDAO extends AbstractSessionDAO {
    
    @Autowired
    RedisManager redisManager;
    //设置session前缀
    private final String SHIRO_SESSION_PREFIX = "alan-session:";

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            //相当于session.setAttribute(key,value)
            String key = getKey(session.getId().toString());
            //将session序列化为byte数组
//            byte[] value = SerializationUtils.serialize(session);
             redisManager.set(key, session);
             redisManager.expire(key, 600);
        }
    }

    //得到session的key
    private String getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);

    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        String key = getKey(session.getId().toString());
         redisManager.del(key);
    }

    //从redis中得到session
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> keys =  redisManager.scan(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (Session key : keys) {
            sessions.add(key);
        }

        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        //生成sessionId
        Serializable sessionId = generateSessionId(session);
        //将session和sessionId捆绑
        assignSessionId(session, sessionId);
        saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session");
        if (sessionId == null) {
            return null;
        }
        String key = getKey(sessionId.toString());
        Object value =  redisManager.get(key);
        return (Session) value;
    }
}
