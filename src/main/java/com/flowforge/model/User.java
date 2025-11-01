package com.flowforge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flowforge.enums.Country;
import com.flowforge.enums.Language;
import com.flowforge.enums.UserStatus;
import com.flowforge.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", unique = true)
    private String userId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.INACTIVE;
    @NotNull(message = "User type cannot be null, please check.")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType = UserType.USER;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userRoles")
    private List<UserRole> userRoles = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country = Country.NIGERIA;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language = Language.ENGLISH;
    @JsonIgnore
    @NotNull(message = "Oops! Password cannot be null, please check.")
    @NotEmpty(message = "Oops! Password empty be null, please check.")
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @NotNull(message = "Activated cannot be null, please check.")
    @Column(name = "activated")
    private Boolean activated = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (userId == null) {
            userId = "USER_" + System.currentTimeMillis();
        }
    }
}
