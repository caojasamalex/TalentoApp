package com.djokic.jobpostservice.model;

import com.djokic.jobpostservice.enumeration.EmploymentTypeEnum;
import com.djokic.jobpostservice.enumeration.JobPostStatusEnum;
import com.djokic.jobpostservice.enumeration.LocationTypeEnum;
import com.djokic.jobpostservice.enumeration.SeniorityLevelEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long recruiterId;

    private LocalDateTime applicationDeadline;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobPostStatusEnum status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentTypeEnum employmentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeniorityLevelEnum seniorityLevel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationTypeEnum locationType;

    private String location;

    private Integer salaryMin;
    private Integer salaryMax;
    private String salaryCurrency;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
