package com.csq.fweb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FwebGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FwebGatewayApplication.class, args);
    }
}    