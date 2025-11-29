package com.djokic.userservice.controller;

import com.djokic.userservice.dto.EditUserDTO;
import com.djokic.userservice.dto.LoginRequestDTO;
import com.djokic.userservice.dto.RegisterRequestDTO;
import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.service.UserService;
import jakarta.validation.Valid;
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
        List<UserDTO> userDTOs = userService.getAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editUser(
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long userId,
            @Valid @RequestBody EditUserDTO editUserDTO
    ){
        UserDTO userDTO = userService.editUser(currentUserId, userId,editUserDTO);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO userDTO = userService.createUser(registerRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        UserDTO userDTO = userService.login(loginRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }
}