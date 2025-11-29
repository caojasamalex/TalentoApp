package com.djokic.companyservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "company-request-service", url = "${COMPANY_REQUEST_SERVICE_URL}")
public interface CompanyRequestServiceClient {
}
