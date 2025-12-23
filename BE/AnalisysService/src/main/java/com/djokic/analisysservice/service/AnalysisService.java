package com.djokic.analisysservice.service;

import com.djokic.analisysservice.client.ApplicationServiceClient;
import com.djokic.analisysservice.client.FileClient;
import com.djokic.analisysservice.client.JobPostServiceClient;
import com.djokic.analisysservice.dto.AiAnalysisResponse;
import com.djokic.analisysservice.dto.ApplicationAnalysisDTO;
import com.djokic.analisysservice.dto.applicationservicedto.ApplicationDTO;
import com.djokic.analisysservice.dto.jobpostservicedto.JobPostDTO;
import com.djokic.analisysservice.mapper.ApplicationAnalysisMapper;
import com.djokic.analisysservice.model.ApplicationAnalysis;
import com.djokic.analisysservice.repository.ApplicationAnalysisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final ApplicationAnalysisRepository applicationAnalysisRepository;
    private final ApplicationAnalysisMapper applicationAnalysisMapper;
    private final JobPostServiceClient jobPostServiceClient;
    private final ApplicationServiceClient applicationServiceClient;
    private final FileClient fileClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Client client = new Client();

    private static final String AI_MODEL = "gemini-1.5-flash";

    public List<ApplicationAnalysisDTO> analyzeJobPostApplications(Long jobPostId, Long currentUserId) throws JsonProcessingException {
        Boolean canManageJobPost = jobPostServiceClient.canManageJobPost(currentUserId, jobPostId);
        if(!canManageJobPost) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to perform this operation!"
            );
        }

        List<ApplicationDTO> applicationDTOList = applicationServiceClient.getApplicationsByJobPostId(jobPostId, currentUserId);
        JobPostDTO jobPostDTO = jobPostServiceClient.getJobPostById(jobPostId);

        List<Long> analyzedApplicationIds = applicationAnalysisRepository
                .findByJobPostId(jobPostId)
                .stream()
                .map(ApplicationAnalysis::getApplicationId)
                .toList();

        List<ApplicationAnalysisDTO> resultList = new ArrayList<>();

        for(ApplicationDTO applicationDTO : applicationDTOList) {
            ApplicationAnalysis analysis;

            if(!analyzedApplicationIds.contains(applicationDTO.getId())) {
                analysis = analyzeSingleApplication(applicationDTO, jobPostDTO);
            } else {
                analysis = applicationAnalysisRepository
                        .findByApplicationId(applicationDTO.getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Analysis exists in set, but not found in DB for application " + applicationDTO.getId()
                        ));
            }

            resultList.add(applicationAnalysisMapper.applicationAnalysisToApplicationAnalysisDTO(analysis));
        }

        return resultList;
    }

    private ApplicationAnalysis analyzeSingleApplication(ApplicationDTO applicationDTO, JobPostDTO jobPostDTO) throws JsonProcessingException {
        String cvText = fetchCvText(applicationDTO.getCvFileUrl());
        String coverLetterText = fetchCoverLetterText(applicationDTO.getCoverLetterFileUrl());

        String prompt = String.format(
                "Analyze this job application for the position: %s.\n\n" +
                        "JOB DESCRIPTION:\n%s\n\n" +
                        "CANDIDATE CV CONTENT:\n%s\n\n" +
                        "CANDIDATE COVER LETTER:\n%s\n\n" +
                        "Output the analysis strictly in JSON format with these fields: " +
                        "'summary' (string), 'strengths' (string), 'weaknesses' (string), 'score' (double between 0 and 100).",
                jobPostDTO.getTitle(),
                jobPostDTO.getDescription(),
                cvText,
                coverLetterText
        );

        GenerateContentConfig aiClientConfig = GenerateContentConfig.builder()
                .maxOutputTokens(1000)
                .temperature(0.2f)
                .responseMimeType("application/json")
                .build();

        GenerateContentResponse response = client.models.generateContent(
                AI_MODEL,
                prompt,
                aiClientConfig
        );

        String jsonText = response.text();
        AiAnalysisResponse aiResponse = objectMapper.readValue(jsonText, AiAnalysisResponse.class);

        ApplicationAnalysis analysis = ApplicationAnalysis.builder()
                .applicationId(applicationDTO.getId())
                .jobPostId(jobPostDTO.getId())
                .score(aiResponse.getScore())
                .summary(aiResponse.getSummary())
                .strengths(aiResponse.getStrengths())
                .weaknesses(aiResponse.getWeaknesses())
                .aiModelVersion(AI_MODEL)
                .analyzedAt(LocalDateTime.now())
                .build();

        return applicationAnalysisRepository.save(analysis);
    }

    public List<ApplicationAnalysisDTO> getAnalyzedJobPostApplications(Long jobPostId, Long currentUserId) {
        Boolean canManageJobPost = jobPostServiceClient.canManageJobPost(jobPostId, currentUserId);
        if(!canManageJobPost) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to perform this operation!"
            );
        }

        return applicationAnalysisRepository
                .findByJobPostId(jobPostId)
                .stream()
                .map(applicationAnalysisMapper::applicationAnalysisToApplicationAnalysisDTO)
                .collect(Collectors.toList());
    }

    private String fetchCvText(String cvFileUrl) {
        if(cvFileUrl == null || cvFileUrl.isBlank()) return "";

        try {
            Path path = Paths.get(cvFileUrl);
            return Files.readString(path);
        } catch (Exception e) {
            return "";
        }
    }

    private String fetchCoverLetterText(String coverLetterFileUrl) {
        if(coverLetterFileUrl == null || coverLetterFileUrl.isBlank()) return "";

        try {
            Path path = Paths.get(coverLetterFileUrl);
            return Files.readString(path);
        } catch (Exception e) {
            return "";
        }
    }

}
