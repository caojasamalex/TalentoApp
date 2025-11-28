package com.djokic.userservice.model;

import com.djokic.userservice.enumeration.PlatformRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,
            nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name",
            nullable = false)
    private String firstName;

    @Column(name = "last_name",
            nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_role", nullable = false)
    private PlatformRole platformRole;
}
