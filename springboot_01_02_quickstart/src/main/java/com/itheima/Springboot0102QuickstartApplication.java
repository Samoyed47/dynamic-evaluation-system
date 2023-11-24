package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.itheima.model")
@ComponentScan(basePackages = "com.itheima.controller")
@ComponentScan(basePackages = "com.itheima.service")
@EnableNeo4jRepositories(basePackages = "com.itheima.repository")
public class Springboot0102QuickstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot0102QuickstartApplication.class, args);
	}

}
