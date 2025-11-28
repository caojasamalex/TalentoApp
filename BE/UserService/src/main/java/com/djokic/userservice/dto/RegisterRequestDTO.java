package com.djokic.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
