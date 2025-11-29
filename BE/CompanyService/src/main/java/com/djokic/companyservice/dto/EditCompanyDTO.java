package com.djokic.companyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EditCompanyDTO {
    private String companyName;
    private String companyPhotoUrl;
    private String companyAddress;
    private String companyCity;
    private String companyWebsite;
    private String companyDescription;
}
