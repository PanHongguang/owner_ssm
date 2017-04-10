package com.cross.dao;

import java.util.List;
import java.util.Map;

import com.cross.pojo.User;

public interface UserDao {

	public List<User> list(Map<String, Object> map);
	
	public Long getTotal(Map<String, Object> map);
}
