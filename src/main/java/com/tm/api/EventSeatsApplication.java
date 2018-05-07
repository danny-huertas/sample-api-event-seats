package com.tm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EventSeatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventSeatsApplication.class, args);
    }
}
