package com.djokic.companyrequestservice.mapper.impl;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.mapper.CompanyRequestMapper;
import com.djokic.companyrequestservice.model.CompanyRequest;
import org.springframework.stereotype.Component;

@Component
public class CompanyRequestMapperImpl implements CompanyRequestMapper {
    @Override
    public CompanyRequestDTO companyRequestToCompanyRequestDTO(CompanyRequest companyRequest) {
        return CompanyRequestDTO
                .builder()
                .id(companyRequest.getId())
                .requestedByUserId(companyRequest.getRequestedByUserId())
                .companyName(companyRequest.getCompanyName())
                .companyAddress(companyRequest.getCompanyAddress())
                .companyCity(companyRequest.getCompanyCity())
                .companyWebsite(companyRequest.getCompanyWebsite())
                .status(companyRequest.getStatus())
                .createdAt(companyRequest.getCreatedAt())
                .updatedAt(companyRequest.getUpdatedAt())
                .build();
    }

    @Override
    public CompanyRequest companyRequestDTOToCompanyRequest(CompanyRequestDTO companyRequestDTO) {
        return CompanyRequest
                .builder()
                .id(companyRequestDTO.getId())
                .requestedByUserId(companyRequestDTO.getRequestedByUserId())
                .companyName(companyRequestDTO.getCompanyName())
                .companyAddress(companyRequestDTO.getCompanyAddress())
                .companyCity(companyRequestDTO.getCompanyCity())
                .companyWebsite(companyRequestDTO.getCompanyWebsite())
                .status(companyRequestDTO.getStatus())
                .createdAt(companyRequestDTO.getCreatedAt())
                .updatedAt(companyRequestDTO.getUpdatedAt())
                .build();
    }
}
