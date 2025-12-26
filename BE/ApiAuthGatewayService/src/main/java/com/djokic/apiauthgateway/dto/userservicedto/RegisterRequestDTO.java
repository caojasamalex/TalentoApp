package com.djokic.apiauthgateway.dto.userservicedto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDTO {

    @Size(max = 255, message = "Email must not be longer than 255 characters!")
    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 50, message = "Password must have at least 8 characters and not be longer than 50!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain a capital letter, a digit and a special character!"
    )
    private String password;

    @Size(max = 50, message = "First must not be longer than 50 characters!")
    @NotBlank(message = "FirstName is required!")
    private String firstName;

    @Size(max = 50, message = "LastName must not be longer than 50 characters!")
    @NotBlank(message = "LastName is required!")
    private String lastName;
}
