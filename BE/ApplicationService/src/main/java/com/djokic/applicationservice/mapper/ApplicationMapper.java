package com.djokic.applicationservice.mapper;

import com.djokic.applicationservice.dto.ApplicationDTO;
import com.djokic.applicationservice.dto.CreateApplicationDTO;
import com.djokic.applicationservice.model.Application;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationMapper {
    public ApplicationDTO applicationToApplicationDto(Application application) {
        ApplicationDTO applicationDTO = ApplicationDTO
                .builder()
                .id(application.getId())
                .userId(application.getUserId())
                .jobPostId(application.getJobPostId())
                .firstName(application.getFirstName())
                .lastName(application.getLastName())
                .email(application.getEmail())
                .cvFileUrl(application.getCvFileUrl())
                .coverLetterText(application.getCoverLetterText())
                .coverLetterFileUrl(application.getCoverLetterFileUrl())
                .createdAt(application.getCreatedAt())
                .build();

        return applicationDTO;
    }

    public Application createApplicationDtoToApplication(CreateApplicationDTO createApplicationDTO) {
        Application application = Application
                .builder()
                .jobPostId(createApplicationDTO.getJobPostId())
                .firstName(createApplicationDTO.getFirstName())
                .lastName(createApplicationDTO.getLastName())
                .email(createApplicationDTO.getEmail())
                .cvFileUrl(createApplicationDTO.getCvFileUrl().toLowerCase().trim())
                .coverLetterText(createApplicationDTO.getCoverLetterText())
                .coverLetterFileUrl(createApplicationDTO.getCoverLetterFileUrl())
                .createdAt(LocalDateTime.now())
                .build();

        return application;
    }
}
