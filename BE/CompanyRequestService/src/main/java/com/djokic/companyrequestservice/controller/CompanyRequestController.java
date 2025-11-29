package com.djokic.companyrequestservice.controller;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.dto.EditCompanyRequestDTO;
import com.djokic.companyrequestservice.service.CompanyRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-requests")
@RequiredArgsConstructor
@Validated
public class CompanyRequestController {
    private final CompanyRequestService companyRequestService;

    @GetMapping
    public ResponseEntity<?> findAllCompanyRequests(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId
    ) {
        List<CompanyRequestDTO> companyRequestDTOS = companyRequestService.findAllCompanyRequests(currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTOS);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> findPendingCompanyRequests(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId
    ) {
        List<CompanyRequestDTO> companyRequestDTOS = companyRequestService.findPendingCompanyRequests(currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyRequestById(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @PathVariable("id") Long companyRequestId
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.findCompanyRequestById(currentUserId, companyRequestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @PostMapping
    public ResponseEntity<?> createCompanyRequest(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Valid @RequestBody CreateCompanyRequestDTO createCompanyRequestDTO
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.createCompanyRequest(currentUserId, createCompanyRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompanyRequest(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @PathVariable("id") Long companyRequestId,
            @RequestBody EditCompanyRequestDTO editCompanyRequestDTO
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.editCompanyRequest(
                currentUserId,
                companyRequestId,
                editCompanyRequestDTO
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveCompanyRequest(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @PathVariable("id") Long companyRequestId
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.approveCompanyRequest(
                currentUserId,
                companyRequestId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectCompanyRequest(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @PathVariable("id") Long companyRequestId
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.rejectCompanyRequest(
                currentUserId,
                companyRequestId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyRequest(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @PathVariable("id") Long companyRequestId
    ) {
        companyRequestService.deleteCompanyRequest(currentUserId, companyRequestId);
        return ResponseEntity.noContent().build();
    }
}