package com.flowforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {
    private Long id;
    private Long taskId;
    private String assigneeId;
    private String assignedBy;
    private String strategy;
    private String reason;
    private LocalDateTime assignedAt;
}
