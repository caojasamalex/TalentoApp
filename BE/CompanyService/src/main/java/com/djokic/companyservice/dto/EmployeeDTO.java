package com.djokic.companyservice.dto;

import com.djokic.companyservice.enumeration.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    private Long userId;
    private Long companyId;
    private EmployeeRole companyRole;
}