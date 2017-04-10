package com.cross.dao;

import java.util.Set;

import com.cross.pojo.TUser;

public interface TUserDao {
	
    TUser findUserByUsername(String username);

    Set<String> findRoles(String username);

    Set<String> findPermissions(String username);
}
