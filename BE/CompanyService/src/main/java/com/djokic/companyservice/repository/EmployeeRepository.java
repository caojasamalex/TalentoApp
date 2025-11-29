package com.djokic.companyservice.repository;

import com.djokic.companyservice.enumeration.EmployeeRole;
import com.djokic.companyservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByUserIdAndCompanyIdAndCompanyRole(Long userId, Long companyId, EmployeeRole companyRole);
}
