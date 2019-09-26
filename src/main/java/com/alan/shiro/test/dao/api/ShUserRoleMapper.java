package com.alan.shiro.test.dao.api;

import com.alan.shiro.test.entity.UserRole;

import java.util.List;

public interface ShUserRoleMapper {

    List<String> selectRole(String username);

    Integer insertRole(UserRole ur);

}
