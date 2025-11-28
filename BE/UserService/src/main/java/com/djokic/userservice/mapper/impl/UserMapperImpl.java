package com.djokic.userservice.mapper.impl;

import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.model.User;
import com.djokic.userservice.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO userToUserDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .platformRole(user.getPlatformRole())
                .build();
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return User
                .builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .platformRole(userDTO.getPlatformRole())
                .build();
    }

    @Override
    public List<UserDTO> userListToUserDTOList(List<User> users) {
        return users
                .stream()
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }
}
