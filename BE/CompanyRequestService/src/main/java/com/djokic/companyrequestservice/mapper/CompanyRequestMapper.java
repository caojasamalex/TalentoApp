package com.djokic.companyrequestservice.mapper;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.model.CompanyRequest;

public interface CompanyRequestMapper {
    CompanyRequestDTO companyRequestToCompanyRequestDTO(CompanyRequest companyRequest);
    CompanyRequest companyRequestDTOToCompanyRequest(CompanyRequestDTO companyRequestDTO);
}
