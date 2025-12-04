package com.djokic.jobpostservice.client;

import com.djokic.jobpostservice.dto.companyservicedto.CompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service-internal", url = "${COMPANY_SERVICE_URL}")
@Component
public interface CompanyServiceInternalClient {
    @GetMapping("/internal/representatives/can-manage-job-posts")
    Boolean canManageJobPosts(@RequestParam Long companyId, @RequestParam Long userId);
}