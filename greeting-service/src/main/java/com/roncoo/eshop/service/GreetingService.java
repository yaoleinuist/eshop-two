package com.roncoo.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GreetingService {

	@Autowired
	private RestTemplate restTemplate;
	
	public String greeting(String name) {
		return restTemplate.getForObject("http://SAY-HELLO-SERVICE/sayHello?name=leo", String.class);
	}
	
}
