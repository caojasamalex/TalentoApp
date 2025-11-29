package com.djokic.companyrequestservice.client;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "company-service", url = "${COMPANY_SERVICE_URL}")
public interface CompanyServiceClient {

    @PostMapping("/companies")
    void createCompany(
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestBody CompanyRequestDTO companyRequestDTO
    );
}
