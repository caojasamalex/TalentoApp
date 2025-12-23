package com.djokic.applicationservice.repository;

import com.djokic.applicationservice.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobPostIdAndRetracted(Long jobPostId, boolean retracted);
}
