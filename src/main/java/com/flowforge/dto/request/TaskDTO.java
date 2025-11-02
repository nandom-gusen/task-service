package com.flowforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private String status;  // TODO, IN_PROGRESS, IN_REVIEW, DONE, CANCELLED
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String assigneeId;
    private String workflowId;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private String notes;
}
