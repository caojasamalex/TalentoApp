package com.djokic.jobpostservice.service;

import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.mapper.JobPostMapper;
import com.djokic.jobpostservice.model.JobPost;
import com.djokic.jobpostservice.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;

    public List<JobPostDTO> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostRepository.findAll();

        return jobPosts.stream().map(jobPostMapper::jobPostToJobPostDTO).collect(Collectors.toList());
    }
}
