package com.djokic.jobpostservice.dto;

import com.djokic.jobpostservice.enumeration.JobPostStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangeJobPostStatusDTO {
    @NotNull(message = "Status is required!")
    private JobPostStatusEnum status;
}
