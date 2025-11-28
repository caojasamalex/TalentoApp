package com.djokic.userservice.dto;

import com.djokic.userservice.enumeration.PlatformRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangeRoleRequestDTO {
    PlatformRole role;
}
