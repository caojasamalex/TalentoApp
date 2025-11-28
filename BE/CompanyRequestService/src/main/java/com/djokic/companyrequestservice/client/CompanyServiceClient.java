package com.djokic.companyrequestservice.client;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "company-service", url = "${COMPANY_SERVICE_URL}")
public interface CompanyServiceClient {

    @PostMapping
    void createCompany(CompanyRequestDTO companyRequestDTO);
}
