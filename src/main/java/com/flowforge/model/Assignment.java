package com.flowforge.model;

import com.flowforge.enums.AssignmentStrategy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Assignment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_id", nullable = false)
    private Long taskId;
    @Column(name = "assignee_id", nullable = false)
    private String assigneeId;
    @Column(name = "assigned_by")
    private String assignedBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "strategy")
    private AssignmentStrategy strategy = AssignmentStrategy.MANUAL;
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

}

