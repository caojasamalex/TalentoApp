package com.djokic.companyservice.dto;

import com.djokic.companyservice.enumeration.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangeEmployeeRoleDTO {
    @NotNull(message = "EmployeeRole is required!")
    private EmployeeRole employeeRole;
}
