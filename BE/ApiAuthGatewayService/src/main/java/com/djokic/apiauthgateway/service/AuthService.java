package com.djokic.apiauthgateway.service;

import com.djokic.apiauthgateway.client.UserServiceClient;
import com.djokic.apiauthgateway.dto.AuthResponse;
import com.djokic.apiauthgateway.dto.userservicedto.LoginRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.RegisterRequestDTO;
import com.djokic.apiauthgateway.dto.userservicedto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceClient userClient;
    private final JwtService jwtService;

    public AuthResponse registerUserAndReturnAuthResponse(@Valid RegisterRequestDTO registerRequestDTO) {
        UserDTO newUser = userClient.registerUser(registerRequestDTO);

        String token = jwtService.generateToken(newUser);

        return new AuthResponse(token, 3600, newUser);
    }

    public AuthResponse loginUserAndReturnAuthResponse(@Valid LoginRequestDTO loginRequestDTO) {
        UserDTO userDTO = userClient.login(loginRequestDTO);

        String token = jwtService.generateToken(userDTO);

        return AuthResponse
                .builder()
                .accessToken(token)
                .expiresIn(3600)
                .userDto(userDTO)
                .build();
    }

    public UserDTO editUser(Long id, @Valid RegisterRequestDTO userDTO) {
        UserDTO userDTOresponse = userClient.updateUser(userDTO, id);

        return userDTOresponse;
    }
}