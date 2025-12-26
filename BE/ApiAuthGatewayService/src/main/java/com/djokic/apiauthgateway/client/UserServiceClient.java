package com.djokic.apiauthgateway.client;

import com.djokic.apiauthgateway.dto.userservicedto.LoginRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.RegisterRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}")
public interface UserServiceClient {

    @PostMapping("/users/register")
    UserDTO registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO);

    @PostMapping("/users/login")
    UserDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO);

    @PutMapping("/users/{id}")
    UserDTO updateUser(@Valid @RequestBody RegisterRequestDTO userDTO, @PathVariable Long id);
}
