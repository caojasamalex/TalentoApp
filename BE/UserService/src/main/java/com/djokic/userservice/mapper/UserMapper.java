package com.djokic.userservice.mapper;

import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.model.User;

import java.util.List;

public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> userListToUserDTOList(List<User> users);
}