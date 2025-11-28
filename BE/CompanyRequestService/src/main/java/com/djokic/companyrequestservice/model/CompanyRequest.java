package com.djokic.companyrequestservice.model;

import com.djokic.companyrequestservice.enumeration.CompanyRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requested_by_user_id", nullable = false)
    private Long requestedByUserId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "company_city", nullable = false)
    private String companyCity;

    @Column(name = "company_website")
    private String companyWebsite;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyRequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
