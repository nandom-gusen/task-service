package com.flowforge.service.assignment;

import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.AssignmentResponse;
import com.flowforge.exceptions.BadRequestException;
import com.flowforge.exceptions.RecordNotFoundException;
import com.flowforge.model.Assignment;
import com.flowforge.model.Task;
import com.flowforge.repository.assignment.AssignmentRepository;
import com.flowforge.repository.task.TaskRepository;
import com.flowforge.utils.EntityMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, TaskRepository taskRepository) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<ApiResponseDto<AssignmentResponse>> createAssignment(AssignmentDTO assignmentDTO) {

        if (assignmentDTO == null) {
            throw new BadRequestException("Assignment request body is invalid");
        }

        if (assignmentDTO.getTaskId() == null) {
            throw new BadRequestException("Task ID is required");
        }

        if (assignmentDTO.getAssigneeId() == null || assignmentDTO.getAssigneeId().trim().isEmpty()) {
            throw new BadRequestException("Assignee ID is required");
        }

        // Validate task exists
        Optional<Task> taskOptional = taskRepository.findById(assignmentDTO.getTaskId());
        if (taskOptional.isEmpty()) {
            throw new RecordNotFoundException("Task with ID " + assignmentDTO.getTaskId() + " not found");
        }

        Assignment assignment = EntityMapperUtil.mapAssignmentDTOtoAssignment(assignmentDTO);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        AssignmentResponse response = EntityMapperUtil.mapAssignmentToAssignmentResponse(savedAssignment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(
                        true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        "Success",
                        response
                ));
    }

    @Override
    public ResponseEntity<ApiResponseDto<List<AssignmentResponse>>> getAssignmentsByTaskId(Long taskId) {

        // Validate task exists
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RecordNotFoundException("Task with ID " + taskId + " not found");
        }

        List<Assignment> assignments = assignmentRepository.findByTaskId(taskId);
        List<AssignmentResponse> responses = assignments.stream()
                .map(EntityMapperUtil::mapAssignmentToAssignmentResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponseDto<>(
                true,
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                responses
        ));
    }

    @Override
    public ResponseEntity<Void> deleteAssignment(Long id) {

        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        if (assignmentOptional.isEmpty()) {
            throw new RecordNotFoundException("Assignment with ID " + id + " not found");
        }

        assignmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
