package com.roncoo.eshop.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roncoo.eshop.service.GreetingService;
import com.roncoo.eshop.service.SayHelloService;

@RestController
public class GreetingController {
	
	Logger logger = LoggerFactory.getLogger(GreetingController.class);
	
	@Autowired
	private GreetingService greetingService;
	
	@Autowired
	private SayHelloService sayHelloService;
	
	@Value("${server.port}")
	private String port;
	
	@Value("${defaultName}")
	private String defaultName;
	
	@RequestMapping("/greeting")
	public String greeting(@RequestParam String name) {
		String result = null;

		if(name != null && !"".equals(name)) { 
			result = sayHelloService.sayHello(name);
		} else {
			result = "hello, this is default name: " + defaultName;
		}
		
		result += " , through greeting service from port: " + port;
		
		return result;
	}
	
	@RequestMapping("/greeting1")
	public String greeting1(@RequestParam String name) {
		String result = null;
		
		result = greetingService.greeting(name);
		
		result += " , through greeting service from port: " + port;
		
		return result;
	}
	
}
