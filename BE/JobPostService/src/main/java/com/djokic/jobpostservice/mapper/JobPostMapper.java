package com.djokic.jobpostservice.mapper;

import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.model.JobPost;

public interface JobPostMapper {
    JobPostDTO jobPostToJobPostDTO(JobPost jobPost);
    JobPost jobPostDTOToJobPost(JobPostDTO jobPostDTO);
}
