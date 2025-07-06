package com.tolimoli.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Welcome to Tolimoli Hotel PMS!";
    }

    @GetMapping("/health")
    public String health() {
        return "PMS is running!";
    }
}