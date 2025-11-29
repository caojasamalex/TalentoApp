package com.djokic.companyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String companyPhotoUrl;
    private String companyAddress;
    private String companyCity;
    private String companyWebsite;
    private String companyDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
