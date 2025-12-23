package com.djokic.analisysservice.repository;

import com.djokic.analisysservice.model.ApplicationAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationAnalysisRepository extends JpaRepository<ApplicationAnalysis, Long> {
    List<ApplicationAnalysis> findByJobPostId(Long jobPostId);

    Optional<ApplicationAnalysis> findByApplicationId(Long applicationId);
}
