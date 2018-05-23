package com.sample.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This is the Spring Boot class for event seats
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EventSeatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventSeatsApplication.class, args);
    }
}
