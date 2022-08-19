package com.example.autos;

import com.example.autos.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AutosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutosApiApplication.class, args);
	}

	@Bean
	public JwtProperties properties() {
		return new JwtProperties();


	}
}