package com.djokic.analisysservice.dto.applicationservicedto;

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
    private boolean retracted;
}