package com.alan.shiro.test.dao.api;

import com.alan.shiro.test.entity.ShUser;
import org.apache.ibatis.annotations.Param;

public interface ShUserMapper {
    ShUser findPassWordByUserName(@Param("username") String username);
    ShUser findUser(@Param("username") String username);
    int insertUser(ShUser user);
}
