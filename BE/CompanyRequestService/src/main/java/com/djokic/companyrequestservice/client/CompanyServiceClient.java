package com.djokic.companyrequestservice.client;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service", url = "${COMPANY_SERVICE_URL}")
public interface CompanyServiceClient {

    @PostMapping("/companies")
    void createCompany(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @RequestParam("companyRequestId") Long companyRequestId
            );
}
