package com.flowforge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO implements Serializable {
    private Long taskId;
    private String assigneeId;
    private String assignedBy;
    private String strategy;
    private String reason;
}
