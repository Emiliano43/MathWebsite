package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheMathWebsite {
    public static void main(String[] args) {
        SpringApplication.run(TheMathWebsite.class, args);
        System.setProperty("java.awt.headless", "false");
    }
}
