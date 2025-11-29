package com.djokic.companyservice.dto.companyrequestservicedto;

import com.djokic.companyservice.enumeration.companyrequestserviceenumeration.CompanyRequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRequestDTO {
    @NotNull(message = "CompanyRequestID is required!")
    @Positive(message = "CompanyRequestID must be positive!")
    private Long id;

    @NotNull(message = "RequestedByUserID is required!")
    @Positive(message = "RequestedByUserID must be positive!")
    private Long requestedByUserId;

    @NotBlank(message = "CompanyName is required!")
    private String companyName;

    @NotBlank(message = "CompanyAddress is required!")
    private String companyAddress;

    @NotBlank(message = "CompanyCity is required!")
    private String companyCity;

    private String companyWebsite;

    @NotNull(message = "Status is required!")
    private CompanyRequestStatus status;

    @NotNull(message = "createdAt is required!")
    private LocalDateTime createdAt;

    @NotNull(message = "updatedAt is required!")
    private LocalDateTime updatedAt;
}