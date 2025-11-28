package com.djokic.userservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
