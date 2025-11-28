package com.djokic.companyrequestservice.client;

import com.djokic.companyrequestservice.dto.userservicedto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}")
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
