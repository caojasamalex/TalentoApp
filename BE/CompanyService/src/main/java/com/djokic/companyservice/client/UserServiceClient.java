package com.djokic.companyservice.client;

import com.djokic.companyservice.dto.userservicedto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDTO findUserById(@PathVariable("id") Long userId);
}
