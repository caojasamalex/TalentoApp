package com.djokic.companyservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}")
public interface UserServiceClient {
}
