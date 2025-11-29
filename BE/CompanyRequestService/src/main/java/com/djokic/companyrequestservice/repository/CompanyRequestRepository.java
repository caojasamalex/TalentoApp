package com.djokic.companyrequestservice.repository;

import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import com.djokic.companyrequestservice.model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, Long> {
    List<CompanyRequest> findCompanyRequestsByStatus(CompanyRequestStatus status);
}
