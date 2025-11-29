package com.djokic.companyservice.mapper.impl;

import com.djokic.companyservice.dto.CompanyDTO;
import com.djokic.companyservice.dto.companyrequestservicedto.CompanyRequestDTO;
import com.djokic.companyservice.mapper.CompanyMapper;
import com.djokic.companyservice.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapperImpl implements CompanyMapper {
    @Override
    public Company companyRequestDTOToCompany(CompanyRequestDTO companyRequestDTO) {
        return Company
                .builder()
                .companyName(companyRequestDTO.getCompanyName())
                .companyAddress(companyRequestDTO.getCompanyAddress())
                .companyCity(companyRequestDTO.getCompanyCity())
                .companyWebsite(companyRequestDTO.getCompanyWebsite())
                .build();
    }

    @Override
    public CompanyDTO companyToCompanyDTO(Company company) {
        return CompanyDTO
                .builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyPhotoUrl(company.getCompanyPhotoUrl())
                .companyAddress(company.getCompanyAddress())
                .companyCity(company.getCompanyCity())
                .companyWebsite(company.getCompanyWebsite())
                .companyDescription(company.getCompanyDescription())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}