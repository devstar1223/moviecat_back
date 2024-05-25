package com.moviecat.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviecatApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoviecatApplication.class, args);
		System.out.println("가동중");
	}

}
