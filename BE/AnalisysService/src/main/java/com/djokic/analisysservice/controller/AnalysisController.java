package com.djokic.analisysservice.controller;

import com.djokic.analisysservice.dto.ApplicationAnalysisDTO;
import com.djokic.analisysservice.service.AnalysisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis-service")
@Validated
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;

    @PostMapping("/job-post/{jobPostId}")
    public ResponseEntity<?> analyzeJobPostApplications(
            @PathVariable("jobPostId") @Positive(message = "Invalid jobPostId!") Long jobPostId,
            @RequestHeader("X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    ) throws JsonProcessingException {
        List<ApplicationAnalysisDTO> applicationAnalysisDTOList = analysisService.analyzeJobPostApplications(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationAnalysisDTOList);
    }

    @GetMapping("/job-post/{jobPostId}")
    public ResponseEntity<?> getAnalyzedJobPostApplications(
            @PathVariable("jobPostId") @Positive(message = "Invalid jobPostId!") Long jobPostId,
            @RequestHeader("X-User-Id") @Positive(message = "Invalid currentUserId!") Long currentUserId
    ) {
        List<ApplicationAnalysisDTO> applicationAnalysisDTOList = analysisService.getAnalyzedJobPostApplications(jobPostId, currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationAnalysisDTOList);
    }
}
