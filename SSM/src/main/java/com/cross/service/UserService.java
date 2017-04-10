package com.cross.service;

import java.util.List;
import java.util.Map;

import com.cross.pojo.User;

public interface UserService {

	/**
	 * 分页查询
	 * @param map
	 * @return
	 */
	public List<User> list(Map<String,Object> map) ;
	
	public Long getTotal(Map<String,Object> map);
}
