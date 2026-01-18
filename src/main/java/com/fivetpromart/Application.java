package com.fivetpromart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling // NEW: Enable scheduled jobs for reservation expiry
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
