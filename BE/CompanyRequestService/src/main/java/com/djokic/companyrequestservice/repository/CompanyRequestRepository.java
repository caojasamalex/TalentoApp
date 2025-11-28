package com.djokic.companyrequestservice.repository;

import com.djokic.companyrequestservice.model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, Long> {
}
