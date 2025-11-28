package com.djokic.companyrequestservice.dto;

import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompanyRequestDTO {
    private Long requestedByUserId;
    private String companyName;
    private String companyAddress;
    private String companyCity;
    private String companyWebsite;
}