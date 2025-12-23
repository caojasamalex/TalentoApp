package com.djokic.jobpostservice.client;

import com.djokic.jobpostservice.dto.applicationservice.ApplicationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "application-service", url = "${APPLICATION_SERVICE_URL}")
public interface ApplicationServiceClient {

    @GetMapping("/applications")
    List<ApplicationDTO> getApplicationsByJobPost(
            @RequestParam("jobPostId") Long jobPostId,
            @RequestHeader("X-User-Id") Long currentUserId
    );
}
