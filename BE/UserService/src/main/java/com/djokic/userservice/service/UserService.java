package com.djokic.userservice.service;

import com.djokic.userservice.dto.*;
import com.djokic.userservice.model.User;
import com.djokic.userservice.enumeration.PlatformRole;
import com.djokic.userservice.mapper.UserMapper;
import com.djokic.userservice.repository.UserRepository;
import com.djokic.userservice.util.HmacSHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HmacSHA256 hmacSHA256;
    private final UserMapper userMapper;


    public UserDTO login(LoginRequestDTO loginRequestDTO) {
        String normalizedEmail = normalizeEmail(loginRequestDTO.getEmail());
        String hashedAndNormalizedPassword = hashAndNormalizePassword(loginRequestDTO.getPassword());

        User user = fetchUserByEmail(normalizedEmail);

        if(!user.getPassword().equals(hashedAndNormalizedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password!");
        }

        return userMapper.userToUserDTO(user);
    }

    @Transactional
    public UserDTO createAdmin(RegisterRequestDTO registerRequestDTO) {
        return registerWithRole(registerRequestDTO, PlatformRole.PLATFORM_ADMIN);
    }

    @Transactional
    public UserDTO createUser(RegisterRequestDTO registerRequestDTO) {
        return registerWithRole(registerRequestDTO, PlatformRole.PLATFORM_USER);
    }

    private UserDTO registerWithRole(RegisterRequestDTO registerRequestDTO, PlatformRole platformRole) {
        String normalizedEmail = normalizeEmail(registerRequestDTO.getEmail());
        String hashedAndNormalizedPassword = hashAndNormalizePassword(registerRequestDTO.getPassword());
        String normalizedFirstName = normalizeFirstName(registerRequestDTO.getFirstName());
        String normalizedLastName = normalizeLastName(registerRequestDTO.getLastName());

        if(userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use!");
        }

        User userToCreate = User.builder()
                .email(normalizedEmail)
                .password(hashedAndNormalizedPassword)
                .firstName(normalizedFirstName)
                .lastName(normalizedLastName)
                .platformRole(platformRole)
                .createdAt(LocalDateTime.now())
                .build();

        return userMapper.userToUserDTO(userRepository.save(userToCreate));
    }

    public UserDTO getUserById(Long userId) {
        validateUserId(userId);

        return userRepository.findUserById(userId)
                .map(userMapper::userToUserDTO)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found!")
                );
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO changeRole(Long currentUserId, Long userId, ChangeRoleRequestDTO changeRoleRequestDTO) {
        validateUserId(currentUserId);
        validateUserId(userId);
        validateAdminRole(currentUserId);

        if(currentUserId.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot change your own role!"
            );
        }

        User user = fetchUserById(userId);

        if(user.getPlatformRole() == changeRoleRequestDTO.getRole()) {
            return userMapper.userToUserDTO(user);
        }

        user.setPlatformRole(changeRoleRequestDTO.getRole());
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO editUser(Long currentUserId, Long userId, EditUserDTO userDTO) {
        validateUserId(currentUserId);
        validateUserId(userId);

        if(!currentUserId.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot change other users' information!"
            );
        }

        User user = fetchUserById(userId);
        boolean hasChanged = false;

        if(userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            String email = normalizeEmail(userDTO.getEmail());

            if(!user.getEmail().equals(email)) {

                if(userRepository.existsByEmail(email)) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Email already in use!"
                    );
                }

                user.setEmail(email);
                hasChanged = true;
            }
        }

        if(userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            String password = hashAndNormalizePassword(userDTO.getPassword());

            if(!user.getPassword().equals(password)) {
                user.setPassword(password);
                hasChanged = true;
            }
        }

        if(userDTO.getFirstName() != null && !userDTO.getFirstName().isBlank()) {
            String firstName = normalizeFirstName(userDTO.getFirstName());

            if(!user.getFirstName().equals(firstName)) {
                user.setFirstName(firstName);
                hasChanged = true;
            }
        }

        if(userDTO.getLastName() != null && !userDTO.getLastName().isBlank()) {
            String lastName = normalizeLastName(userDTO.getLastName());

            if(!user.getLastName().equals(lastName)) {
                user.setLastName(lastName);
                hasChanged = true;
            }
        }

        if(hasChanged) {
            return userMapper.userToUserDTO(userRepository.save(user));
        }

        return userMapper.userToUserDTO(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String hashAndNormalizePassword(String password) {
        return hmacSHA256.hashPassword(password.trim());
    }

    private String normalizeFirstName(String firstName) {
        return firstName.trim();
    }

    private String normalizeLastName(String lastName) {
        return lastName.trim();
    }

    private void validateUserId(Long userId) {
        if(userId == null || userId <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UserID!");
        }
    }

    private void validateAdminRole(Long currentUserId) {
        User user = fetchUserById(currentUserId);
        if(user.getPlatformRole() != PlatformRole.PLATFORM_ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only platform admin can perform this action!"
            );
        }
    }

    private User fetchUserByEmail(String email) {
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " not found!")
                );
    }

    private User fetchUserById(Long userId){
        return userRepository
                .findUserById(userId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found!")
                );
    }
}
