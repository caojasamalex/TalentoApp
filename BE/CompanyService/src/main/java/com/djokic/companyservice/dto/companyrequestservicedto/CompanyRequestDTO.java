package com.djokic.companyservice.dto.companyrequestservicedto;

import com.djokic.companyservice.enumeration.companyrequestserviceenumeration.CompanyRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRequestDTO {
    private Long id;
    private Long requestedByUserId;
    private String companyName;
    private String companyAddress;
    private String companyCity;
    private String companyWebsite;
    private CompanyRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}