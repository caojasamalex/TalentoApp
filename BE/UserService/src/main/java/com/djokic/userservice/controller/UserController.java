package com.djokic.userservice.controller;

import com.djokic.userservice.dto.LoginRequestDTO;
import com.djokic.userservice.dto.RegisterRequestDTO;
import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO dto = userService.register(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserDTO dto = userService.login(loginRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}