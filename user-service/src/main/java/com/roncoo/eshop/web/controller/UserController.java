package com.roncoo.eshop.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.roncoo.eshop.model.User;
import com.roncoo.eshop.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAllUsers")
	@ResponseBody
	public List<User> findAllUsers() {
		return userService.findAllUsers();
	}
	
}
