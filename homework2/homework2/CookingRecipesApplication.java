package com.example.homework2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CookingRecipesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CookingRecipesApplication.class, args);
    }
}
