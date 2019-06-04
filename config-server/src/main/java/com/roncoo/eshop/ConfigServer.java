package com.roncoo.eshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServer {

	Logger logger = LoggerFactory.getLogger(ConfigServer.class);
	
    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }
    
}