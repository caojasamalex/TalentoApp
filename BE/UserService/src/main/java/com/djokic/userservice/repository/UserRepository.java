package com.djokic.userservice.repository;

import com.djokic.userservice.dto.UserDTO;
import com.djokic.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserById(Long id);
}
