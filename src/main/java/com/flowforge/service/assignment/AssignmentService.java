package com.flowforge.service.assignment;

import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.AssignmentResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssignmentService {
    ResponseEntity<ApiResponseDto<AssignmentResponse>> createAssignment(AssignmentDTO assignmentDTO);
    ResponseEntity<ApiResponseDto<List<AssignmentResponse>>> getAssignmentsByTaskId(Long taskId);
    ResponseEntity<Void> deleteAssignment(Long id);
}
