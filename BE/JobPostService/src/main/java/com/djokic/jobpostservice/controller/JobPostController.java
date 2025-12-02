package com.djokic.jobpostservice.controller;

import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-posts")
@RequiredArgsConstructor
@Validated
public class JobPostController {
    private final JobPostService jobPostService;

    @GetMapping
    public ResponseEntity<?> getAllJobPosts(){
        List<JobPostDTO> jobPostDTOS = jobPostService.getAllJobPosts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTOS);
    }
}
