package com.flowforge.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flowforge.enums.Permissions;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(unique = true, name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @JsonSerialize
    @JsonDeserialize
    @Type(JsonStringType.class)
    @Column(name = "permissions", columnDefinition = "JSON", nullable = false, updatable = true)
    private Set<Permissions> permissions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private List<UserRole> userRoles;
}

