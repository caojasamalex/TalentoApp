package com.djokic.companyrequestservice.dto.userservicedto;

import com.djokic.companyrequestservice.enumeration.userserviceenumeration.PlatformRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private PlatformRole platformRole;
}