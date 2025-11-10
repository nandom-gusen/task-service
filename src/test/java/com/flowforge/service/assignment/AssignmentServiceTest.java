package com.flowforge.service.assignment;

import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.AssignmentResponse;
import com.flowforge.enums.AssignmentStrategy;
import com.flowforge.exceptions.BadRequestException;
import com.flowforge.exceptions.RecordNotFoundException;
import com.flowforge.model.Assignment;
import com.flowforge.model.Task;
import com.flowforge.repository.assignment.AssignmentRepository;
import com.flowforge.repository.task.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAssignment_shouldCreateAndReturnAssignment() {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(1L);
        assignmentDTO.setAssigneeId("USER_123");
        assignmentDTO.setAssignedBy("USER_456");
        assignmentDTO.setStrategy("MANUAL");
        assignmentDTO.setReason("Best fit");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        Assignment assignment = new Assignment();
        assignment.setId(1L);
        assignment.setTaskId(1L);
        assignment.setAssigneeId("USER_123");
        assignment.setAssignedBy("USER_456");
        assignment.setStrategy(AssignmentStrategy.MANUAL);
        assignment.setAssignedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(assignmentRepository.save(ArgumentMatchers.<Assignment>any())).thenReturn(assignment);

        // Act
        ResponseEntity<ApiResponseDto<AssignmentResponse>> response = assignmentService.createAssignment(assignmentDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        AssignmentResponse assignmentResponse = response.getBody().getData();
        assertNotNull(assignmentResponse);
        assertEquals(1L, assignmentResponse.getId());
        assertEquals(1L, assignmentResponse.getTaskId());
        assertEquals("USER_123", assignmentResponse.getAssigneeId());

        verify(taskRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(ArgumentMatchers.isA(Assignment.class));
    }

    @Test
    void createAssignment_shouldThrowException_whenTaskNotFound() {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(999L);
        assignmentDTO.setAssigneeId("USER_123");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            assignmentService.createAssignment(assignmentDTO);
        });

        assertEquals("Task with ID 999 not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(999L);
        verify(assignmentRepository, never()).save(ArgumentMatchers.isA(Assignment.class));
    }

    @Test
    void createAssignment_shouldThrowException_whenAssigneeIdIsEmpty() {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(1L);
        assignmentDTO.setAssigneeId("   ");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            assignmentService.createAssignment(assignmentDTO);
        });

        assertEquals("Assignee ID is required", exception.getMessage());
        verify(taskRepository, never()).findById(anyLong());
        verify(assignmentRepository, never()).save(ArgumentMatchers.isA(Assignment.class));
    }

    @Test
    void getAssignmentsByTaskId_shouldReturnAssignmentsList() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        Assignment assignment1 = new Assignment();
        assignment1.setId(1L);
        assignment1.setTaskId(taskId);
        assignment1.setAssigneeId("USER_123");

        Assignment assignment2 = new Assignment();
        assignment2.setId(2L);
        assignment2.setTaskId(taskId);
        assignment2.setAssigneeId("USER_456");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(assignmentRepository.findByTaskId(taskId)).thenReturn(Arrays.asList(assignment1, assignment2));

        // Act
        ResponseEntity<ApiResponseDto<List<AssignmentResponse>>> response = assignmentService.getAssignmentsByTaskId(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        List<AssignmentResponse> assignments = response.getBody().getData();
        assertNotNull(assignments);
        assertEquals(2, assignments.size());
        assertEquals("USER_123", assignments.get(0).getAssigneeId());
        assertEquals("USER_456", assignments.get(1).getAssigneeId());

        verify(taskRepository, times(1)).findById(taskId);
        verify(assignmentRepository, times(1)).findByTaskId(taskId);
    }

    @Test
    void getAssignmentsByTaskId_shouldThrowException_whenTaskNotFound() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            assignmentService.getAssignmentsByTaskId(taskId);
        });

        assertEquals("Task with ID 999 not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
        verify(assignmentRepository, never()).findByTaskId(anyLong());
    }

    @Test
    void deleteAssignment_shouldDeleteAssignment() {
        // Arrange
        Long assignmentId = 1L;
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        // Act
        ResponseEntity<Void> response = assignmentService.deleteAssignment(assignmentId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(assignmentRepository, times(1)).findById(assignmentId);
        verify(assignmentRepository, times(1)).deleteById(assignmentId);
    }

    @Test
    void deleteAssignment_shouldThrowException_whenAssignmentNotFound() {
        // Arrange
        Long assignmentId = 999L;
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            assignmentService.deleteAssignment(assignmentId);
        });

        assertEquals("Assignment with ID 999 not found", exception.getMessage());
        verify(assignmentRepository, times(1)).findById(assignmentId);
        verify(assignmentRepository, never()).deleteById(anyLong());
    }
}
