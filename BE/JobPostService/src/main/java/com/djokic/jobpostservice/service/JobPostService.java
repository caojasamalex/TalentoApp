package com.djokic.jobpostservice.service;

import com.djokic.jobpostservice.dto.CreateJobPostDTO;
import com.djokic.jobpostservice.dto.EditJobPostDTO;
import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.mapper.JobPostMapper;
import com.djokic.jobpostservice.model.JobPost;
import com.djokic.jobpostservice.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;

    public List<JobPostDTO> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostRepository.findAll();

        return jobPosts
                .stream()
                .map(jobPostMapper::jobPostToJobPostDTO)
                .collect(Collectors.toList());
    }

    public List<JobPostDTO> getJobPostByTitle(String title){
        if(title == null || title.isBlank()){
            return getAllJobPosts();
        }

        String normalizedTitle = title.toLowerCase().trim();

        List<JobPost> jobPosts = jobPostRepository.findJobPostsByTitleContainsIgnoreCase(normalizedTitle);

        return jobPosts
                .stream()
                .map(jobPostMapper::jobPostToJobPostDTO)
                .collect(Collectors.toList());
    }

    public List<JobPostDTO> getJobPostsByCompanyId(Long companyId){
        List<JobPost> jobPosts = jobPostRepository.findJobPostsByCompanyId(companyId);

        return jobPosts
                .stream()
                .map(jobPostMapper::jobPostToJobPostDTO)
                .collect(Collectors.toList());
    }

    public JobPostDTO getJobPostById(Long id){
        JobPost jobPost = jobPostRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "JobPost with id " + id + " not found!"
                        )
                );

        return jobPostMapper.jobPostToJobPostDTO(jobPost);
    }

    public JobPostDTO createJobPost(CreateJobPostDTO createJobPostDTO){
        if(createJobPostDTO == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CreateJobPostBody is required!"
            );
        }

        JobPost jobPost = jobPostMapper.createJobPostDTOToJobPost(createJobPostDTO);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return jobPostMapper.jobPostToJobPostDTO(savedJobPost);
    }

    public JobPostDTO editJobPost(Long jobPostId, Long currentUserId,EditJobPostDTO editJobPostDTO){
        //TODO: Check if the user is TALENT_ACQUISTION_MANAGER, CHECK IF THE JOBPOST EXISTS AND THEN, IF IT DOES
        // MAKE CHANGES TO THE JOBPOST.

        return null;
    }
}
