package com.alan.shiro.test.service;

import com.alan.shiro.test.comm.CallResult;
import com.alan.shiro.test.comm.CommUtils;
import com.alan.shiro.test.comm.DateUtils;
import com.alan.shiro.test.comm.Global;
import com.alan.shiro.test.dao.api.ShUserMapper;
import com.alan.shiro.test.dao.api.ShUserRoleMapper;
import com.alan.shiro.test.entity.ShUser;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;


/**
 * 可根据此类对用户 进行扩展操作
 */
@Service
public class UserService {
    @Autowired
    ShUserMapper shUserMapper;
    @Autowired
    ShUserRoleMapper shUserRoleMapper;

    /**
     *
     * @param username
     * @return
     */
    public ShUser getUserPassword(String username){
        System.out.println("从数据库中拿数据");
      ShUser shUser =   shUserMapper.findPassWordByUserName(username);
      return shUser;
    }

    public List<String> getUserRole(String username){
        List<String> list = shUserRoleMapper.selectRole(username);
        return list;
    }

    public ShUser getUser(String username){
        ShUser u = shUserMapper.findUser(username);
        return u;
    }

    public CallResult registerUser(ShUser user){
        CallResult cr = new CallResult(-1);
        if(CommUtils.isEmpty(user.getUsername())){
            return cr.setMsg("用户名为空");
        }
        if(CommUtils.isEmpty(user.getPassword())){
            return cr.setMsg("密码为空");
        }
        user.setCreateTime(DateUtils.getNowTimeStr());
        user.setId(UUID.randomUUID().toString());
        this.encryptionPassword(user);
        shUserMapper.insertUser(user);
        return new CallResult(0);
    }

    private ShUser encryptionPassword(ShUser user){
        user.setHash(new SecureRandomNumberGenerator().nextBytes().toHex());
        String newPassword = new SimpleHash(Global.ENCRYPTION,user.getPassword(),
                ByteSource.Util.bytes(user.getHash()), Global.HASHITERATIONS).toHex();
        user.setPassword(newPassword);
        return user;
    }
}
