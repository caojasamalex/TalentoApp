package com.djokic.companyservice.controller;

import com.djokic.companyservice.service.EmployeeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/representatives")
@RequiredArgsConstructor
public class EmployeeInternalController {
    private final EmployeeService employeeService;

    @GetMapping("/can-manage-job-posts")
    public ResponseEntity<Boolean> existsEmployee(
            @Positive(message = "Invalid CompanyID!") @RequestParam Long companyId,
            @Positive(message = "Invalid CompanyID!") @RequestParam Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.canManageJobPosts(companyId, userId));
    }
}
