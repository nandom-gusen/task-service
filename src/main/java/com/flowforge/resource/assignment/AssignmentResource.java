package com.flowforge.resource.assignment;

import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.AssignmentResponse;
import com.flowforge.service.assignment.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@Tag(name = "Assignment resource", description = "Resource for Task Assignments")
public class AssignmentResource {

    private final AssignmentService assignmentService;

    public AssignmentResource(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @Operation(summary = "Create Assignment", description = "Assign a task to a user")
    @ApiResponse(responseCode = "201", description = "Assignment created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<AssignmentResponse>> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get Task Assignments", description = "Retrieve all assignments for a task")
    @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<List<AssignmentResponse>>> getAssignmentsByTaskId(@PathVariable Long taskId) {
        return assignmentService.getAssignmentsByTaskId(taskId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Assignment", description = "Remove an assignment")
    @ApiResponse(responseCode = "204", description = "Assignment deleted successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        return assignmentService.deleteAssignment(id);
    }
}
