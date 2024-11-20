package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.file.props;

@SpringBootApplication
@EnableConfigurationProperties({
	props.class
})
public class LoginApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}
	
}
