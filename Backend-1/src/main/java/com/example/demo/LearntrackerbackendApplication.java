package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.example.deno.config.WebSocketConfig;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "other.package.if.needed"})
@Import({ WebSocketConfig.class})
public class LearntrackerbackendApplication {

	public static void main(String[] args) {
		 
		SpringApplication.run(LearntrackerbackendApplication.class, args);
	}

}
