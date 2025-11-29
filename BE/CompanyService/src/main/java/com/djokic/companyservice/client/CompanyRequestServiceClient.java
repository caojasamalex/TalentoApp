package com.djokic.companyservice.client;

import com.djokic.companyservice.dto.companyrequestservicedto.CompanyRequestDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "company-request-service", url = "${COMPANY_REQUEST_SERVICE_URL}")
public interface CompanyRequestServiceClient {

    @GetMapping("/company-requests/{id}")
    CompanyRequestDTO findCompanyRequestById(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid companyRequestID!") @PathVariable("id") Long companyRequestId
    );
}
