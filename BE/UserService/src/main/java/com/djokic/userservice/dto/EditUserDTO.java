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
    @Size(max = 255, message = "Email must not be longer than 255 characters!")
    @Email(message = "Invalid email format!")
    private String email;

    @Size(min = 8, max = 50, message = "Password must have at least 8 characters and not be longer than 50!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain a capital letter, a digit and a special character!"
    )
    private String password;

    @Size(max = 50, message = "FirstName must not be longer than 50 characters!")
    private String firstName;

    @Size(max = 50, message = "LastName must not be longer than 50 characters!")
    private String lastName;
}