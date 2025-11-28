package com.djokic.companyrequestservice.controller;

import com.djokic.companyrequestservice.dto.CompanyRequestDTO;
import com.djokic.companyrequestservice.dto.CreateCompanyRequestDTO;
import com.djokic.companyrequestservice.service.CompanyRequestService;
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
    public ResponseEntity<?> findAllCompanyRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findAllCompanyRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCompanyRequestById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.findCompanyRequestById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCompanyRequest(@RequestBody CreateCompanyRequestDTO createCompanyRequestDTO) {
        CompanyRequestDTO companyRequestDTO = companyRequestService.createCompanyRequest(createCompanyRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyRequestDTO);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveCompanyRequest(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyRequestService.approveCompanyRequest(id));
    }
}
