package com.djokic.companyservice.service;

import com.djokic.companyservice.enumeration.EmployeeRole;
import com.djokic.companyservice.repository.EmployeeRepository;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public boolean existsByCompanyIdAndUserIdAndRole(Long companyId, Long userId, EmployeeRole role) {
        return employeeRepository.existsByUserIdAndCompanyIdAndCompanyRole(userId, companyId, role);
    }

    public Boolean canManageJobPosts(Long companyId, Long userId) {
        return existsByCompanyIdAndUserIdAndRole(companyId, userId, EmployeeRole.TALENT_ACQUISITION);
    }
}
