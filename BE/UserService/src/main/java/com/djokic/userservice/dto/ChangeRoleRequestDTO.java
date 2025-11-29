package com.djokic.userservice.dto;

import com.djokic.userservice.enumeration.PlatformRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangeRoleRequestDTO {
    @NotNull(message = "PlatformRole is required!")
    private PlatformRole role;
}
