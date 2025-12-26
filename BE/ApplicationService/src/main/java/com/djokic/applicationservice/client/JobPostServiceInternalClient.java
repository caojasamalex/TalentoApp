package com.djokic.applicationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "job-post-service-internal", url = "${JOB_POST_SERVICE_URL}")
public interface JobPostServiceInternalClient {
    @GetMapping("/job-posts/internal/can-manage")
    Boolean canManageJobPost(
            @RequestParam Long jobPostId,
            @RequestHeader("X-User-Id") Long currentUserId
    );
}