package com.djokic.analisysservice.dto.jobpostservicedto;

import com.djokic.analisysservice.enumeration.jobpostserviceenumeration.EmploymentTypeEnum;
import com.djokic.analisysservice.enumeration.jobpostserviceenumeration.JobPostStatusEnum;
import com.djokic.analisysservice.enumeration.jobpostserviceenumeration.LocationTypeEnum;
import com.djokic.analisysservice.enumeration.jobpostserviceenumeration.SeniorityLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JobPostDTO {
    private Long id;
    private Long companyId;
    private Long recruiterId;
    private String title;
    private String description;
    private JobPostStatusEnum status;
    private EmploymentTypeEnum employmentType;
    private SeniorityLevelEnum seniorityLevel;
    private LocationTypeEnum locationType;
    private String location;
    private Integer salaryMax;
    private Integer salaryMin;
    private String salaryCurrency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
