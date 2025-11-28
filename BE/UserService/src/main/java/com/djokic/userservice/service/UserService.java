package com.djokic.userservice.service;

import com.djokic.userservice.dto.ChangeRoleRequestDTO;
import com.djokic.userservice.dto.LoginRequestDTO;
import com.djokic.userservice.dto.RegisterRequestDTO;
import com.djokic.userservice.dto.UserDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HmacSHA256 hmacSHA256;
    private final UserMapper userMapper;
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Transactional
    public UserDTO register(RegisterRequestDTO registerRequestDTO) {
        validateAndNormalizeRegisterRequest(registerRequestDTO);

        User userToCreate = User.builder()
                .email(registerRequestDTO.getEmail())
                .password(hmacSHA256.hashPassword(registerRequestDTO.getPassword()))
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .platformRole(PlatformRole.PLATFORM_USER)
                .build();

        return userMapper.userToUserDTO(userRepository.save(userToCreate));
    }

    public UserDTO login(LoginRequestDTO loginRequestDTO) {
        validateAndNormalizeLoginRequest(loginRequestDTO);

        User user = userRepository.findUserByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(!hmacSHA256.hashPassword(loginRequestDTO.getPassword()).equals(user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return userMapper.userToUserDTO(user);
    }

    @Transactional
    public UserDTO createAdmin(RegisterRequestDTO registerRequestDTO) {
        validateAndNormalizeRegisterRequest(registerRequestDTO);

        User userToCreate = User.builder()
                .email(registerRequestDTO.getEmail())
                .password(hmacSHA256.hashPassword(registerRequestDTO.getPassword()))
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .platformRole(PlatformRole.PLATFORM_ADMIN)
                .build();

        return userMapper.userToUserDTO(userRepository.save(userToCreate));
    }

    public UserDTO getUserById(Long id) {
        if(id <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        UserDTO userDTO = userRepository.findUserById(id)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such ID doesn't exist"));

        return userDTO;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    private void validateAndNormalizeRegisterRequest(RegisterRequestDTO registerRequestDTO) {
        // Validate email - is null ? is blank ? invalid format ?
        // Validate firstName - is null ? is blank ? invalid characters present ? length exceeded ?
        // Validate secondName - is null ? is blank ? invalid characters present ? length exceeded ?
        // Validate password - is null ? is blank ? minimum of 8 characters ? a special character, a number and a capitalized letter present ?
        validateEmail(registerRequestDTO.getEmail());
        validatePassword(registerRequestDTO.getPassword());
        validateFirstName(registerRequestDTO.getFirstName());
        validateLastName(registerRequestDTO.getLastName());

        // Normalizing the input
        registerRequestDTO.setEmail(registerRequestDTO.getEmail().toLowerCase().trim());
        registerRequestDTO.setPassword(registerRequestDTO.getPassword().trim());
        registerRequestDTO.setFirstName(registerRequestDTO.getFirstName().trim());
        registerRequestDTO.setLastName(registerRequestDTO.getLastName().trim());

        // Check if the email has already been used
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
    }

    private void validateAndNormalizeLoginRequest(LoginRequestDTO loginRequestDTO) {
        // Validate email - is null ? is blank ? invalid format ?
        // Validate password - is null ? is blank ? minimum of 8 characters ? a special character, a number and a capitalized letter present ?
        validateEmail(loginRequestDTO.getEmail());

        if(loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }

        // Normalizing the input
        loginRequestDTO.setEmail(loginRequestDTO.getEmail().toLowerCase().trim());
        loginRequestDTO.setPassword(loginRequestDTO.getPassword().trim());
    }

    private void validateEmail(String email) {
        if(email == null || email.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required!");
        }

        email = email.trim();

        if(!email.matches(EMAIL_REGEX)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email!");
        }
    }

    private void validatePassword(String password) {
        if(password == null || password.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required!");
        }
        password = password.trim();

        boolean hasCapital = false; boolean hasDigit = false; boolean hasSpecial = false;

        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)){
                hasCapital = true;
            } else if (Character.isDigit(c)){
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)){
                hasSpecial = true;
            }
        }

        if(!hasCapital) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one capital letter!");
        if(!hasDigit) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one digit!");
        if(!hasSpecial) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one special character!");
        if(password.length() < 8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters!");
    }

    private void validateFirstName(String firstName) {
        if(firstName == null || firstName.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required!");
        }

        firstName = firstName.trim();

        // TODO: Validate that first name doesn't contain special characters
    }

    private void validateLastName(String lastName) {
        if(lastName == null || lastName.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required!");
        }

        lastName = lastName.trim();

        // TODO: Validate that last name doesn't contain special characters
    }

    public UserDTO changeRole(Long userId, ChangeRoleRequestDTO changeRoleRequestDTO) {
        if(userId == null || userId <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id must be greater than zero!");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPlatformRole(changeRoleRequestDTO.getRole());

        return userMapper.userToUserDTO(userRepository.save(user));
    }
}
