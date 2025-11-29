package com.djokic.userservice.controller;

import com.djokic.userservice.dto.ChangeRoleRequestDTO;
import com.djokic.userservice.dto.RegisterRequestDTO;
import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Object> createAdmin(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDTO createdAdmin = userService.createAdmin(registerRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAdmin);
    }

    @PatchMapping("/user/{id}/role")
    public ResponseEntity<Object> changeUserRole(
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long userId,
            @Valid @RequestBody ChangeRoleRequestDTO changeRoleRequestDTO
    ) {
        UserDTO userDTO = userService.changeRole(currentUserId, userId, changeRoleRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }
}
