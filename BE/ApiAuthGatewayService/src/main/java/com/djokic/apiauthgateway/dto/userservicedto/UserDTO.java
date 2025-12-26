package com.djokic.apiauthgateway.dto.userservicedto;

import com.djokic.apiauthgateway.enumeration.userserviceenumeration.PlatformRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private PlatformRole platformRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
