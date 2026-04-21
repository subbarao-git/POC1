package com.example.myjavaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyJavaAppApplication {

    @GetMapping("/")
    public String home() {
        return "CI/CD Pipeline for Java Application is working 🚀";
    }

    public static void main(String[] args) {
        SpringApplication.run(MyJavaAppApplication.class, args);
    }
}
