package com.djokic.jobpostservice.controller;

import com.djokic.jobpostservice.dto.CreateJobPostDTO;
import com.djokic.jobpostservice.dto.EditJobPostDTO;
import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.service.JobPostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-posts")
@RequiredArgsConstructor
@Validated
public class JobPostController {
    private final JobPostService jobPostService;


    @GetMapping
    public ResponseEntity<?> searchJobPosts(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String title) {
        List<JobPostDTO> jobPostDTOS = jobPostService.search(companyId, title);
        return ResponseEntity.ok(jobPostDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobPostById(@PathVariable Long id){
        JobPostDTO jobPostDTO = jobPostService.getJobPostById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTO);
    }

    @PostMapping
    public ResponseEntity<?> createJobPost(@Valid @RequestBody CreateJobPostDTO createJobPostDTO){
        JobPostDTO jobPostDTO = jobPostService.createJobPost(createJobPostDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobPostDTO);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> editJobPost(
            @Positive(message = "Invalid JobPostID!") @PathVariable("id") Long jobPostId,
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Valid @RequestBody EditJobPostDTO editJobPostDTO){
        JobPostDTO jobPostDTO = jobPostService.editJobPost(jobPostId, currentUserId, editJobPostDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTO);
    }
}
