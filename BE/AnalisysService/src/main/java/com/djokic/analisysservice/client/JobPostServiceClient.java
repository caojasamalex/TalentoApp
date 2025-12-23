package com.djokic.analisysservice.client;

import com.djokic.analisysservice.dto.jobpostservicedto.JobPostDTO;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "job-post-service", url = "${JOB_POST_SERVICE_URL}")
public interface JobPostServiceClient {
    @GetMapping("/job-posts/internal/can-manage")
    Boolean canManageJobPost(
            @RequestParam Long jobPostId,
            @RequestHeader("X-User-Id") Long currentUserId
    );

    @GetMapping("/job-posts/{id}")
    JobPostDTO getJobPostById(@PathVariable("id") @Positive(message = "Invalid jobPostId!") Long jobPostId);
}