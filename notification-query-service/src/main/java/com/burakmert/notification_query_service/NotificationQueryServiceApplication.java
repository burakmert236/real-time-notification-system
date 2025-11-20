package com.burakmert.notification_query_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NotificationQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationQueryServiceApplication.class, args);
    }

}
