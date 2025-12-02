package com.djokic.jobpostservice.mapper.impl;

import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.mapper.JobPostMapper;
import com.djokic.jobpostservice.model.JobPost;
import org.springframework.stereotype.Component;

@Component
public class JobPostMapperImpl implements JobPostMapper {
    @Override
    public JobPostDTO jobPostToJobPostDTO(JobPost jobPost) {
        return JobPostDTO
                .builder()
                .id(jobPost.getId())
                .companyId(jobPost.getCompanyId())
                .recruiterId(jobPost.getRecruiterId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .status(jobPost.getStatus())
                .employmentType(jobPost.getEmploymentType())
                .seniorityLevel(jobPost.getSeniorityLevel())
                .locationType(jobPost.getLocationType())
                .location(jobPost.getLocation())
                .salaryMin(jobPost.getSalaryMin())
                .salaryMax(jobPost.getSalaryMax())
                .salaryCurrency(jobPost.getSalaryCurrency())
                .createdAt(jobPost.getCreatedAt())
                .updatedAt(jobPost.getUpdatedAt())
                .build();
    }

    @Override
    public JobPost jobPostDTOToJobPost(JobPostDTO jobPostDTO) {
        return JobPost
                .builder()
                .id(jobPostDTO.getId())
                .companyId(jobPostDTO.getCompanyId())
                .recruiterId(jobPostDTO.getRecruiterId())
                .title(jobPostDTO.getTitle())
                .description(jobPostDTO.getDescription())
                .status(jobPostDTO.getStatus())
                .employmentType(jobPostDTO.getEmploymentType())
                .seniorityLevel(jobPostDTO.getSeniorityLevel())
                .locationType(jobPostDTO.getLocationType())
                .location(jobPostDTO.getLocation())
                .salaryMin(jobPostDTO.getSalaryMin())
                .salaryMax(jobPostDTO.getSalaryMax())
                .salaryCurrency(jobPostDTO.getSalaryCurrency())
                .createdAt(jobPostDTO.getCreatedAt())
                .updatedAt(jobPostDTO.getUpdatedAt())
                .build();
    }
}