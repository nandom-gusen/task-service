package com.flowforge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Role roleId;
    @JoinColumn(name = "user_roles", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User userRoles;
}

