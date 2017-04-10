package com.cross.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cross.dao.UserDao;
import com.cross.pojo.User;
import com.cross.service.UserService;

@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> list(Map<String, Object> map) {
		return userDao.list(map);
	}
	
	@Override
	public Long getTotal(Map<String, Object> map) {
		return userDao.getTotal(map);
	}

}
