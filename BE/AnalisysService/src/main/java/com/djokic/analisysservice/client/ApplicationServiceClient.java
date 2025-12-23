package com.djokic.analisysservice.client;

import com.djokic.analisysservice.dto.applicationservicedto.ApplicationDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "application-service", url = "${APPLICATION_SERVICE_URL}")
public interface ApplicationServiceClient {

    @GetMapping("/by-job-post")
    List<ApplicationDTO> getApplicationsByJobPostId(
            @RequestParam("jobPostId") Long jobPostId,
            @RequestHeader("X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    );
}
