package com.djokic.companyrequestservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompanyRequestDTO {
    @NotBlank(message = "CompanyName is required!")
    private String companyName;

    @NotBlank(message = "CompanyAddress is required!")
    private String companyAddress;

    @NotBlank(message = "CompanyCity is required!")
    private String companyCity;

    private String companyWebsite;
}