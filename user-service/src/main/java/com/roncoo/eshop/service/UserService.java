package com.roncoo.eshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roncoo.eshop.mapper.UserMapper;
import com.roncoo.eshop.model.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public List<User> findAllUsers() {
		return userMapper.findAllUsers();
	}
	
}
