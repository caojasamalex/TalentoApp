package com.djokic.companyservice.client;

import com.djokic.companyservice.dto.companyrequestservicedto.CompanyRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-request-service", url = "${COMPANY_REQUEST_SERVICE_URL}")
public interface CompanyRequestServiceClient {

    @GetMapping("/company-requests/{id}")
    CompanyRequestDTO findCompanyRequestById(@PathVariable("id") Long companyRequestId);
}
