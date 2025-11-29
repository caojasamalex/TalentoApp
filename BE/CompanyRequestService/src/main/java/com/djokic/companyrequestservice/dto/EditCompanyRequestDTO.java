package com.djokic.companyrequestservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCompanyRequestDTO {
    private String companyName;
    private String companyAddress;
    private String companyCity;
    private String companyWebsite;
}