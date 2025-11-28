package com.djokic.userservice.controller;

import com.djokic.userservice.dto.ChangeRoleRequest;
import com.djokic.userservice.dto.RegisterRequestDTO;
import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO createdAdmin = userService.createAdmin(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PatchMapping("/user/{id}/role")
    public ResponseEntity<?> changeUserRole(
            @PathVariable("id") Long userId,
            @RequestBody ChangeRoleRequest changeRoleRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeRole(userId, changeRoleRequest));
    }
}
