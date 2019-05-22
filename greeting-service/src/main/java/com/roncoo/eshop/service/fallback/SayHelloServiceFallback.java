package com.roncoo.eshop.service.fallback;

import org.springframework.stereotype.Component;

import com.roncoo.eshop.service.SayHelloService;

@Component
public class SayHelloServiceFallback implements SayHelloService {

	public String sayHello(String name) {
		return "error, " + name;
	}

}
