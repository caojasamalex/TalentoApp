package com.djokic.apiauthgateway.dto;

import com.djokic.apiauthgateway.dto.userservicedto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private long expiresIn;
    UserDTO userDto;
}
