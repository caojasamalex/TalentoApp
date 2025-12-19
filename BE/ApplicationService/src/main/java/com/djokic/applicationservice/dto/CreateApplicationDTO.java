package com.djokic.applicationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateApplicationDTO {

    @NotNull(message = "JobPostId is required!")
    private Long jobPostId;

    @NotBlank(message = "FirstName is required!")
    private String firstName;

    @NotBlank(message = "LastName is required!")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid Email format!")
    private String email;

    @NotBlank(message = "cvFileUrl is Required!")
    private String cvFileUrl;

    private String coverLetterText;
    private String coverLetterFileUrl;
}