package com.flowforge.model;

import com.flowforge.enums.TaskStatus;
import com.flowforge.utils.TaskStatusListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow_states")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowState  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude  // Prevent circular toString
    @EqualsAndHashCode.Exclude  // Prevent circular equals
    private Workflow workflow;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;
    @Column(name = "requires_assignee")
    private Boolean requiresAssignee = false;
    @Convert(converter = TaskStatusListConverter.class)
    @Column(name = "next_states", columnDefinition = "TEXT")
    private List<TaskStatus> nextStates = new ArrayList<>();

}

