package com.rhapps.show_of_hands.showofhands;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ShowOfHandsApplication {


	public static void main(String[] args) {
		SpringApplication.run(ShowOfHandsApplication.class, args);
	}

}
