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

    @GetMapping("/{id}/applications")
    public ResponseEntity<?> getApplicationsByJobPost(
            @PathVariable("id") @Positive(message = "Invalid JobPostID!") Long jobPostId,
            @RequestHeader("X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostService.getApplicationsByJobPost(jobPostId, currentUserId));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<?> publishJobPost(
            @Positive(message = "Invalid currentUserId!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid JobPostID!") @PathVariable("id") Long jobPostId
    ){
        JobPostDTO jobPostDTO = jobPostService.publishJobPost(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTO);
    }

    @PatchMapping("/{id}/draft")
    public ResponseEntity<?> draftJobPost(
            @Positive(message = "Invalid currentUserId!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid JobPostID!") @PathVariable("id") Long jobPostId
    ){
        JobPostDTO jobPostDTO = jobPostService.draftJobPost(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTO);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<?> archiveJobPost(
            @Positive(message = "Invalid currentUserId!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid JobPostID!") @PathVariable("id") Long jobPostId
    ){
        JobPostDTO jobPostDTO = jobPostService.archiveJobPost(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobPostDTO);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<?> closeJobPost(
            @Positive(message = "Invalid currentUserId!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid JobPostID!") @PathVariable("id") Long jobPostId
    ){
        JobPostDTO jobPostDTO = jobPostService.closeJobPost(jobPostId, currentUserId);

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

    @GetMapping("/internal/can-manage")
    public ResponseEntity<Boolean> canManageJobPost(
            @RequestParam("jobPostId") @Positive Long jobPostId,
            @RequestHeader("X-User-Id") @Positive Long currentUserId
    ){
        return ResponseEntity.ok(
                jobPostService.canManageJobPost(jobPostId, currentUserId)
        );
    }
}
