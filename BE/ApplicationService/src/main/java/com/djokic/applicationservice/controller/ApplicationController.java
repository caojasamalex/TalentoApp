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

@RestController
@RequestMapping("/applications")
@Validated
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationDTO> submitApplication(
            @RequestBody CreateApplicationDTO createApplicationDTO,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        ApplicationDTO applicationDTO = applicationService.submitApplication(createApplicationDTO, currentUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationDTO);
    }
}
