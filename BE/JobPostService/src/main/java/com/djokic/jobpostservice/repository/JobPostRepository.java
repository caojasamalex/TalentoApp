package com.djokic.jobpostservice.repository;

import com.djokic.jobpostservice.enumeration.JobPostStatusEnum;
import com.djokic.jobpostservice.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findJobPostsByTitleContainsIgnoreCase(String title);
    List<JobPost> findJobPostsByCompanyIdAndStatus(Long companyId, JobPostStatusEnum status);
    List<JobPost> findJobPostsByCompanyId(Long companyId);
}