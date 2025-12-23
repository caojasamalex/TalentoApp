package com.djokic.analisysservice.mapper;

import com.djokic.analisysservice.dto.ApplicationAnalysisDTO;
import com.djokic.analisysservice.model.ApplicationAnalysis;
import org.springframework.stereotype.Component;


@Component
public class ApplicationAnalysisMapper {
    public ApplicationAnalysisDTO applicationAnalysisToApplicationAnalysisDTO(ApplicationAnalysis applicationAnalysis) {
        return ApplicationAnalysisDTO
                .builder()
                .id(applicationAnalysis.getId())
                .applicationId(applicationAnalysis.getApplicationId())
                .jobPostId(applicationAnalysis.getJobPostId())
                .score(applicationAnalysis.getScore())
                .summary(applicationAnalysis.getSummary())
                .strengths(applicationAnalysis.getStrengths())
                .weaknesses(applicationAnalysis.getWeaknesses())
                .aiModelVersion(applicationAnalysis.getAiModelVersion())
                .analyzedAt(applicationAnalysis.getAnalyzedAt())
                .build();
    }

    public ApplicationAnalysis applicationAnalysisDTOToApplicationAnalysis(ApplicationAnalysis applicationAnalysis) {
        return ApplicationAnalysis
                .builder()
                .id(applicationAnalysis.getId())
                .applicationId(applicationAnalysis.getApplicationId())
                .jobPostId(applicationAnalysis.getJobPostId())
                .score(applicationAnalysis.getScore())
                .summary(applicationAnalysis.getSummary())
                .strengths(applicationAnalysis.getStrengths())
                .weaknesses(applicationAnalysis.getWeaknesses())
                .aiModelVersion(applicationAnalysis.getAiModelVersion())
                .analyzedAt(applicationAnalysis.getAnalyzedAt())
                .build();
    }
}