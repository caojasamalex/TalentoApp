package com.djokic.applicationservice.service;

import com.djokic.applicationservice.dto.ApplicationDTO;
import com.djokic.applicationservice.dto.CreateApplicationDTO;
import com.djokic.applicationservice.mapper.ApplicationMapper;
import com.djokic.applicationservice.model.Application;
import com.djokic.applicationservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationMapper applicationMapper;
    private final ApplicationRepository applicationRepository;

    public ApplicationDTO submitApplication(CreateApplicationDTO createApplicationDTO, Long currentUserId) {
        if(currentUserId != null && currentUserId <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid currentUserId!"
            );
        }

        boolean hasText = createApplicationDTO.getCoverLetterText() != null && !createApplicationDTO.getCoverLetterText().isBlank();
        boolean hasFile = createApplicationDTO.getCoverLetterFileUrl() != null && !createApplicationDTO.getCoverLetterFileUrl().isBlank();

        if (!hasText && !hasFile) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cover letter is required (text or file)"
            );
        }

        if (hasText && hasFile) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only one cover letter option is allowed"
            );
        }

        Application application = applicationMapper.createApplicationDtoToApplication(createApplicationDTO);
        application.setUserId(currentUserId);

        if(hasText) application.setCoverLetterText(application.getCoverLetterText().trim());
        if(hasFile) application.setCoverLetterFileUrl(application.getCoverLetterFileUrl().toLowerCase().trim());

        return applicationMapper.applicationToApplicationDto(applicationRepository.save(application));
    }
}
