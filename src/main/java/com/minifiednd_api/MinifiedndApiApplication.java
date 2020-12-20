package com.minifiednd_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.minifiednd_api"})
public class MinifiedndApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MinifiedndApiApplication.class, args);
	}

}
