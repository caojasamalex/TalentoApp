package com.djokic.apiauthgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiAuthGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAuthGatewayApplication.class, args);
    }
}
