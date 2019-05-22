package com.roncoo.eshop.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.roncoo.eshop.service.fallback.SayHelloServiceFallback;

@FeignClient(value = "say-hello-service", fallback = SayHelloServiceFallback.class)  
public interface SayHelloService {

	@RequestMapping(value = "/sayHello", method = RequestMethod.GET)
	public String sayHello(@RequestParam(value = "name") String name);
	
}
