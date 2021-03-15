package com.iwill.fastJson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FastJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastJsonApplication.class, args);
    }
}
