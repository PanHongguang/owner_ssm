package com.cross.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cross.dao.TUserDao;
import com.cross.pojo.TUser;
import com.cross.service.TUserService;

@Service("tUserService")
@Transactional
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserDao tUserDao ;

    @Override
    public TUser findUserByUsername(String username) {
        TUser tuser = tUserDao.findUserByUsername(username);
        return tuser;
    }

    @Override
    public Set<String> findRoles(String username) {
        return tUserDao.findRoles(username);
    }

    @Override
    public Set<String> findPermissions(String username) {
        return tUserDao.findPermissions(username);
    }
}
