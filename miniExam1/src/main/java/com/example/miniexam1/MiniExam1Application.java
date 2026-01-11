package com.example.miniexam1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MiniExam1Application {
    public static void main(String[] args) {
        SpringApplication.run(MiniExam1Application.class, args);
    }
}
