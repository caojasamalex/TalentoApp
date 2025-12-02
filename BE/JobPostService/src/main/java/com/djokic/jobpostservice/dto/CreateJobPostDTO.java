package com.djokic.jobpostservice.dto;

import com.djokic.jobpostservice.enumeration.EmploymentTypeEnum;
import com.djokic.jobpostservice.enumeration.JobPostStatusEnum;
import com.djokic.jobpostservice.enumeration.LocationTypeEnum;
import com.djokic.jobpostservice.enumeration.SeniorityLevelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateJobPostDTO {

    @NotNull(message = "CompanyID is required!")
    @Positive(message = "Invalid CompanyID!")
    private Long companyId;

    @NotNull(message = "RecruiterID is required!")
    @Positive(message = "Invalid CompanyID!")
    private Long recruiterId;

    @NotBlank(message = "Title is required!")
    private String title;

    @NotBlank(message = "Description is required!")
    private String description;

    @NotNull(message = "EmploymentType is required!")
    private EmploymentTypeEnum employmentType;

    @NotNull(message = "SeniorityLevel is required!")
    private SeniorityLevelEnum seniorityLevel;

    @NotNull(message = "LocationType is required!")
    private LocationTypeEnum locationType;

    private String location;

    @Positive(message = "SalaryMax must be greater than zero!")
    private Integer salaryMax;

    @Positive(message = "SalaryMin must be greated than zero!")
    private Integer salaryMin;

    private String salaryCurrency;
}