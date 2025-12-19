package com.djokic.applicationservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplicationDTO {
    private Long id;
    private Long userId;
    private Long jobPostId;
    private String firstName;
    private String lastName;
    private String email;

    private String cvFileUrl;

    private String coverLetterText;
    private String coverLetterFileUrl;

    private LocalDateTime createdAt;
}