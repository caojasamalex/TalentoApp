package com.djokic.applicationservice.controller;

import com.djokic.applicationservice.dto.ApplicationDTO;
import com.djokic.applicationservice.dto.CreateApplicationDTO;
import com.djokic.applicationservice.service.ApplicationService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@Validated
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/by-job-post")
    public ResponseEntity<?> getApplicationsByJobPostId(
            @RequestParam("jobPostId") Long jobPostId,
            @RequestHeader("X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    ){
        List<ApplicationDTO> applicationDTOList = applicationService.getApplicationsByJobPostId(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationDTOList);
    }

    @PostMapping
    public ResponseEntity<?> submitApplication(
            @RequestBody CreateApplicationDTO createApplicationDTO,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        ApplicationDTO applicationDTO = applicationService.submitApplication(createApplicationDTO, currentUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationDTO);
    }

    @PatchMapping("/{id}/retract")
    public ResponseEntity<?> retractApplication(
            @PathVariable("id") @Positive(message = "Invalid applicationId!") Long applicationId,
            @RequestHeader(value = "X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    ){
        ApplicationDTO applicationDTO = applicationService.retractApplication(applicationId, currentUserId);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationDTO);
    }
}
