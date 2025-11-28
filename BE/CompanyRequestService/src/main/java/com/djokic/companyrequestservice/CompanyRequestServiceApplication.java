package com.djokic.companyrequestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompanyRequestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyRequestServiceApplication.class, args);
    }

}
