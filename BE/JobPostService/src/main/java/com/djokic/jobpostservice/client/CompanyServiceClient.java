package com.djokic.jobpostservice.client;

import com.djokic.jobpostservice.dto.companyservicedto.CompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "${COMPANY_SERVICE_URL}")
public interface CompanyServiceClient {
    @GetMapping("/companies/{id}")
    CompanyDTO getCompanyById(@PathVariable Long id);
}
