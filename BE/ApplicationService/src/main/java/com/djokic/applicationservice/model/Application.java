package com.djokic.applicationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "applications",
        uniqueConstraints ={
            @UniqueConstraint(
                    name = "uk_application_job_email",
                    columnNames = {"job_post_id", "email"}
            )
        }
)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Column(name = "job_post_id", nullable = false)
    private Long jobPostId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cv_file_url", nullable = false)
    private String cvFileUrl;

    @Lob
    @Column(name = "cover_letter_text", nullable = true)
    private String coverLetterText;

    @Column(name = "cover_letter_file_url", nullable = true)
    private String coverLetterFileUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
