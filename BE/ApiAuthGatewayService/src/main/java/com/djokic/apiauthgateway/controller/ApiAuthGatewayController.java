package com.djokic.apiauthgateway.controller;

import com.djokic.apiauthgateway.dto.AuthResponse;
import com.djokic.apiauthgateway.dto.userservicedto.LoginRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.RegisterRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.UserDTO;
import com.djokic.apiauthgateway.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class ApiAuthGatewayController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        AuthResponse authResponse = authService.registerUserAndReturnAuthResponse(registerRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        AuthResponse authResponse = authService.loginUserAndReturnAuthResponse(loginRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterRequestDTO userDTO) {
        UserDTO userDTOresponse = authService.editUser(id, userDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTOresponse);
    }
}