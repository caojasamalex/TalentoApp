package com.djokic.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EditUserDTO {
    @Email(message = "Invalid email format!")
    private String email;

    @Size(min = 8, message = "Password must have at least 8 characters!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain a capital letter, a digit and a special character!"
    )
    private String password;
    private String firstName;
    private String lastName;
}