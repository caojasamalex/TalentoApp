package com.djokic.companyservice.controller;

import com.djokic.companyservice.dto.CompanyDTO;
import com.djokic.companyservice.dto.EditCompanyDTO;
import com.djokic.companyservice.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyDTOS = companyService.findAllCompanies();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(
            @Positive(message = "Invalid CompanyID!") @PathVariable("id") Long companyId
    ) {
        CompanyDTO companyDTO = companyService.findCompanyById(companyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllCompaniesByCompanyName(
            @RequestParam(name = "companyName", required = false) String companyName
    ){
        List<CompanyDTO> companyDTOS = companyService.findAllCompaniesByCompanyName(companyName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyDTOS);
    }

    @PostMapping
    public ResponseEntity<?> createCompany(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyRequestID!") @RequestParam("companyRequestId") Long companyRequestId
    ) {
        CompanyDTO companyDTO = companyService.createCompany(currentUserId, companyRequestId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @Positive(message = "Invalid CurrentUserID!") @RequestHeader("X-User-Id") Long currentUserId,
            @Positive(message = "Invalid CompanyID!") @PathVariable("id") Long companyId,
            @Valid @RequestBody EditCompanyDTO editCompanyDTO
    ) {
        CompanyDTO companyDTO = companyService.editCompany(currentUserId, companyId, editCompanyDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(companyDTO);
    }
}
