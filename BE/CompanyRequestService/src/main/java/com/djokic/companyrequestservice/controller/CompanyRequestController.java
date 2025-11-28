package com.djokic.companyrequestservice.controller;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.service.CompanyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-requests")
@RequiredArgsConstructor
public class CompanyRequestController {
    private final CompanyRequestService companyRequestService;

    @GetMapping
    public ResponseEntity<?> findAllCompanyRequests(@RequestHeader("X-User-Id") Long currentUserId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findAllCompanyRequests(currentUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyRequestById(@RequestHeader("X-User-Id") Long currentUserId, @PathVariable("id") Long companyRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findCompanyRequestById(currentUserId, companyRequestId));
    }

    @PostMapping
    public ResponseEntity<?> createCompanyRequest(@RequestHeader("X-User-Id") Long currentUserId, @RequestBody CreateCompanyRequestDTO createCompanyRequestDTO) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.createCompanyRequest(currentUserId, createCompanyRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompanyRequest(@RequestHeader("X-User-Id") Long currentUserId, @PathVariable("id") Long companyRequestId, @RequestBody CompanyRequestDTO companyRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.editCompanyRequest(currentUserId, companyRequestId, companyRequestDTO));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveCompanyRequest(@RequestHeader("X-User-Id") Long currentUserId, @PathVariable("id") Long companyRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.approveCompanyRequest(currentUserId, companyRequestId));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectCompanyRequest(@RequestHeader("X-User-Id") Long currentUserId, @PathVariable("id") Long companyRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.rejectCompanyRequest(currentUserId, companyRequestId));
    }
}
