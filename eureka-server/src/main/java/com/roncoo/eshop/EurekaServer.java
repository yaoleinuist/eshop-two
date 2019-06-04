package com.roncoo.eshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServer {

	Logger logger = LoggerFactory.getLogger(EurekaServer.class);
	
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}
	
}
