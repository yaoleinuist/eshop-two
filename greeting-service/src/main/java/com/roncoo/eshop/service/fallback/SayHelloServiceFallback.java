package com.roncoo.eshop.service.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.roncoo.eshop.service.SayHelloService;

@Component
public class SayHelloServiceFallback implements SayHelloService {

	Logger logger = LoggerFactory.getLogger(SayHelloServiceFallback.class);
	
	public String sayHello(String name) {
		logger.info("#SayHelloServiceFallback--name={}",name);
		return "error1, " + name;
	}

}
