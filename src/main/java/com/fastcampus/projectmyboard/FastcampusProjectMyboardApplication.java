package com.fastcampus.projectmyboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FastcampusProjectMyboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastcampusProjectMyboardApplication.class, args);
    }

}


