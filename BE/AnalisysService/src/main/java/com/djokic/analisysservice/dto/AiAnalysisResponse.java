package com.djokic.analisysservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiAnalysisResponse {
    private String summary;
    private String strengths;
    private String weaknesses;
    private Double score;
}
