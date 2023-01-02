package com.xinnn.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Reggis {
    public static void main(String[] args) {
        SpringApplication.run(Reggis.class, args);
    }
}
