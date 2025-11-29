package com.djokic.companyrequestservice.controller;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.dto.EditCompanyRequestDTO;
import com.djokic.companyrequestservice.service.CompanyRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-requests")
@RequiredArgsConstructor
public class CompanyRequestController {
    private final CompanyRequestService companyRequestService;

    @GetMapping
    public ResponseEntity<?> findAllCompanyRequests(@RequestHeader("X-User-Id") Long currentUserId) {
        List<CompanyRequestDTO> companyRequestDTO = companyRequestService.findAllCompanyRequests(currentUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyRequestById(
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long companyRequestId
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.findCompanyRequestById(currentUserId, companyRequestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyRequestDTO);
    }

    @PostMapping
    public ResponseEntity<?> createCompanyRequest(
            @RequestHeader("X-User-Id") Long currentUserId,
            @Valid @RequestBody CreateCompanyRequestDTO createCompanyRequestDTO
    ) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.createCompanyRequest(currentUserId, createCompanyRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompanyRequest(
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long companyRequestId,
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
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long companyRequestId
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
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long companyRequestId
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
            @RequestHeader("X-User-Id") Long currentUserId,
            @PathVariable("id") Long companyRequestId
    ) {
        companyRequestService.deleteCompanyRequest(currentUserId, companyRequestId);
        return ResponseEntity.noContent().build();
    }
}