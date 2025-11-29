package com.djokic.companyservice.mapper;

import com.djokic.companyservice.dto.CompanyDTO;
import com.djokic.companyservice.dto.companyrequestservicedto.CompanyRequestDTO;
import com.djokic.companyservice.model.Company;

public interface CompanyMapper {
    Company companyRequestDTOToCompany(CompanyRequestDTO companyRequestDTO);
    CompanyDTO companyToCompanyDTO(Company company);
}
