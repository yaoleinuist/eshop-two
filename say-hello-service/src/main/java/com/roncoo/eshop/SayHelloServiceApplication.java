package com.roncoo.eshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
public class SayHelloServiceApplication {

	Logger logger = LoggerFactory.getLogger(SayHelloServiceApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SayHelloServiceApplication.class, args); 
	}
	
	@Value("${server.port}")
	private String port;
	
	@RequestMapping("/sayHello")
	@HystrixCommand(fallbackMethod = "sayHelloFallback")
	public String sayHello(String name) {
		return "hello, " + name + " from port: " + port;
	}
	
	public String sayHelloFallback(String name) {
		logger.info("#SayHelloServiceApplication--name={}",name);
		return "error2, " + name;
	}
	
}
