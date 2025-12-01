package com.djokic.userservice.model;

import com.djokic.userservice.enumeration.PlatformRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,
            nullable = false,
            length = 255)
    private String email;

    @Column(nullable = false,
            length = 50)
    private String password;

    @Column(name = "first_name",
            nullable = false,
            length = 100)
    private String firstName;

    @Column(name = "last_name",
            nullable = false,
            length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_role", nullable = false)
    private PlatformRole platformRole;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
