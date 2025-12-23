package com.djokic.analisysservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "application_analysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="application_id", nullable = false)
    private Long applicationId;

    @Column(name = "job_post_id", nullable = false)
    private Long jobPostId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "summary", nullable = false, length = 4000)
    private String summary;

    @Column(name = "strengths", nullable = false, length = 4000)
    private String strengths;

    @Column(name = "weaknesses", nullable = false, length = 4000)
    private String weaknesses;

    @Column(name = "ai_model_version", nullable = false)
    private String aiModelVersion;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;
}
