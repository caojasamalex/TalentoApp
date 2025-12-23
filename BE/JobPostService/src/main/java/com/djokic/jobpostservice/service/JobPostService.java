package com.djokic.jobpostservice.service;

import com.djokic.jobpostservice.client.ApplicationServiceClient;
import com.djokic.jobpostservice.client.CompanyServiceClient;
import com.djokic.jobpostservice.client.CompanyServiceInternalClient;
import com.djokic.jobpostservice.dto.CreateJobPostDTO;
import com.djokic.jobpostservice.dto.EditJobPostDTO;
import com.djokic.jobpostservice.dto.JobPostDTO;
import com.djokic.jobpostservice.dto.applicationservice.ApplicationDTO;
import com.djokic.jobpostservice.enumeration.JobPostStatusEnum;
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
    private final ApplicationServiceClient applicationServiceClient;
    private final CompanyServiceClient companyServiceClient;
    private final CompanyServiceInternalClient companyServiceInternalClient;

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
        JobPost jobPost = jobPostMapper.createJobPostDTOToJobPost(createJobPostDTO);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return jobPostMapper.jobPostToJobPostDTO(savedJobPost);
    }

    public JobPostDTO editJobPost(Long jobPostId, Long currentUserId, EditJobPostDTO editJobPostDTO){
        //TODO: Check if the user is TALENT_ACQUISTION_MANAGER, CHECK IF THE JOBPOST EXISTS AND THEN, IF IT DOES
        // MAKE CHANGES TO THE JOBPOST.

        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "JobPost with id " + jobPostId + " not found!"
                )
        );

        Boolean canManageJobPost = companyServiceInternalClient.canManageJobPosts(jobPost.getCompanyId(), currentUserId);
        if(!canManageJobPost){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Not Authorized!"
            );
        }

        boolean hasChanged = false;

        if(editJobPostDTO.getTitle() != null
                && !editJobPostDTO.getTitle().isBlank()
                    && !editJobPostDTO.getTitle().trim().equals(jobPost.getTitle())){
            String normalizedTitle = editJobPostDTO.getTitle().trim();
            jobPost.setTitle(normalizedTitle);
            hasChanged = true;
        }

        if(editJobPostDTO.getDescription() != null
                && !editJobPostDTO.getDescription().isBlank()
                    && !editJobPostDTO.getDescription().trim().equals(jobPost.getDescription())){
            String normalizedDescription = editJobPostDTO.getDescription().trim();
            jobPost.setDescription(normalizedDescription);
            hasChanged = true;
        }

        if(editJobPostDTO.getEmploymentType() != null
                && editJobPostDTO.getEmploymentType() != jobPost.getEmploymentType()){
            jobPost.setEmploymentType(editJobPostDTO.getEmploymentType());
            hasChanged = true;
        }

        if(editJobPostDTO.getSeniorityLevel() != null
                && editJobPostDTO.getSeniorityLevel() != jobPost.getSeniorityLevel()){
            jobPost.setSeniorityLevel(editJobPostDTO.getSeniorityLevel());
            hasChanged = true;
        }

        if(editJobPostDTO.getLocationType() != null
                && editJobPostDTO.getLocationType() != jobPost.getLocationType()){
            jobPost.setLocationType(editJobPostDTO.getLocationType());
            hasChanged = true;
        }

        if(editJobPostDTO.getLocation() != null
                && !editJobPostDTO.getLocation().isBlank()
                    && !editJobPostDTO.getLocation().trim().equals(jobPost.getLocation())){
            String normalizedLocation = editJobPostDTO.getLocation().trim();
            jobPost.setLocation(normalizedLocation);
            hasChanged = true;
        }

        if(editJobPostDTO.getSalaryMax() != null
                && !editJobPostDTO.getSalaryMax().equals(jobPost.getSalaryMax())){
            jobPost.setSalaryMax(editJobPostDTO.getSalaryMax());
            hasChanged = true;
        }

        if(editJobPostDTO.getSalaryMin() != null
                && !editJobPostDTO.getSalaryMin().equals(jobPost.getSalaryMin())){
            jobPost.setSalaryMin(editJobPostDTO.getSalaryMin());
            hasChanged = true;
        }

        if(editJobPostDTO.getSalaryCurrency() != null
                && !editJobPostDTO.getSalaryCurrency().isBlank()
                && !editJobPostDTO.getSalaryCurrency().trim().equals(jobPost.getSalaryCurrency())){
            jobPost.setSalaryCurrency(editJobPostDTO.getSalaryCurrency());
            hasChanged = true;
        }

        if(hasChanged){
            return jobPostMapper.jobPostToJobPostDTO(jobPostRepository.save(jobPost));
        }

        return jobPostMapper.jobPostToJobPostDTO(jobPost);
    }

    public List<JobPostDTO> search(Long companyId, String title) {
        if(companyId != null && companyId <= 0L){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid CompanyID!"
            );
        }

        if(companyId == null && (title == null || title.isBlank())){
            return getAllJobPosts();
        }

        if(companyId == null){
            return getJobPostByTitle(title);
        }

        if(title == null || title.isBlank()){
            return getJobPostsByCompanyId(companyId);
        }

        String normalizedTitle = title.trim();

        List<JobPost> jobPosts = jobPostRepository.findJobPostsByCompanyIdAndTitleContainsIgnoreCase(companyId, normalizedTitle);

        return jobPosts
                .stream()
                .map(jobPostMapper::jobPostToJobPostDTO)
                .collect(Collectors.toList());
    }

    public JobPostDTO publishJobPost(Long jobPostId, Long currentUserId) {
        return changeJobPostStatus(jobPostId, currentUserId, JobPostStatusEnum.ACTIVE);
    }

    public JobPostDTO draftJobPost(Long jobPostId, Long currentUserId) {
        return changeJobPostStatus(jobPostId, currentUserId, JobPostStatusEnum.DRAFT);
    }

    public JobPostDTO archiveJobPost(Long jobPostId, Long currentUserId) {
        return changeJobPostStatus(jobPostId, currentUserId, JobPostStatusEnum.ARCHIVED);
    }

    public JobPostDTO closeJobPost(Long jobPostId, Long currentUserId) {
        return changeJobPostStatus(jobPostId, currentUserId, JobPostStatusEnum.CLOSED);
    }

    private JobPostDTO changeJobPostStatus(Long jobPostId, Long currentUserId, JobPostStatusEnum status) {
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Post Not Found")
        );

        Boolean canManageJobPosts = companyServiceInternalClient.canManageJobPosts(jobPost.getCompanyId(), currentUserId);
        if(!canManageJobPosts){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied!"
            );
        }

        if(jobPost.getStatus().equals(JobPostStatusEnum.CLOSED)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Job Post has been closed!");
        }

        jobPost.setStatus(status);

        return jobPostMapper.jobPostToJobPostDTO(jobPostRepository.save(jobPost));
    }

    public List<ApplicationDTO> getApplicationsByJobPost(Long jobPostId, Long currentUserId) {
        Long companyId = jobPostRepository
                .findById(jobPostId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Invalid JobPostID!"
                        )
                )
                .getCompanyId();

        Boolean canManageJobPosts = companyServiceInternalClient.canManageJobPosts(companyId, currentUserId);
        if(!canManageJobPosts){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied!"
            );
        }

        return applicationServiceClient.getApplicationsByJobPost(jobPostId, currentUserId);
    }

    public boolean canManageJobPost(Long jobPostId, Long currentUserId) {
        Long companyId = jobPostRepository
                .findById(jobPostId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Invalid JobPostID!"
                        )
                )
                .getCompanyId();

        return companyServiceInternalClient.canManageJobPosts(companyId, currentUserId);
    }
}
