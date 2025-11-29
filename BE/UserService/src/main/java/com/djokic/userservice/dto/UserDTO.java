package com.djokic.userservice.dto;

import com.djokic.userservice.enumeration.PlatformRole;
import lombok.*;

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
}
