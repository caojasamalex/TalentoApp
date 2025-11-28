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
    public ResponseEntity<?> findAllCompanyRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findAllCompanyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyRequestById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findCompanyRequestById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCompanyRequest(@RequestHeader("CurrentUserId") Long currentUserId, @RequestBody CreateCompanyRequestDTO createCompanyRequestDTO) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.createCompanyRequest(currentUserId, createCompanyRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompanyRequest(@RequestHeader("CurrentUserID") Long currentUserId, @PathVariable("id") Long companyRequestId, @RequestBody CompanyRequestDTO companyRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.editCompanyRequest(currentUserId, companyRequestId, companyRequestDTO));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveCompanyRequest(@RequestHeader("CurrentUserID") Long currentUserId, @PathVariable("id") Long companyRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.approveCompanyRequest(currentUserId, companyRequestId));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectCompanyRequest(@RequestHeader("CurrentUserID") Long currentUserId, @PathVariable("id") Long companyRequestId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.rejectCompanyRequest(currentUserId, companyRequestId));
    }
}
