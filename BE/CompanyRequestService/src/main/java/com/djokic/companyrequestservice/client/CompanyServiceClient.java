package com.djokic.companyrequestservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "company-service", url = "${COMPANY_SERVICE_URL}")
public interface CompanyServiceClient {
}
