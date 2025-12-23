package com.djokic.analisysservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationAnalysisDTO {
    Long id;
    Long applicationId;
    Long jobPostId;
    Double score;

    String summary;
    String strengths;
    String weaknesses;

    String aiModelVersion;
    LocalDateTime analyzedAt;
}
