package com.flowforge.resource.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowforge.config.SpringDataWebConfig;
import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.AssignmentResponse;
import com.flowforge.exceptions.BadRequestException;
import com.flowforge.exceptions.GlobalExceptionHandler;
import com.flowforge.exceptions.RecordNotFoundException;
import com.flowforge.security.jwt.AuthTokenFilter;
import com.flowforge.service.assignment.AssignmentService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SpringDataWebConfig.class, GlobalExceptionHandler.class})
@WebMvcTest(controllers = AssignmentResource.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AssignmentResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;

    @MockitoBean
    private AssignmentService assignmentService;

    @BeforeEach
    void forwardFilterChain() throws Exception {
        doAnswer(invocation -> {
            ServletRequest req = invocation.getArgument(0);
            ServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(authTokenFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    void createAssignment_shouldReturnCreatedAssignment() throws Exception {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(1L);
        assignmentDTO.setAssigneeId("USER_123");
        assignmentDTO.setAssignedBy("USER_456");
        assignmentDTO.setStrategy("MANUAL");

        AssignmentResponse assignmentResponse = new AssignmentResponse();
        assignmentResponse.setId(1L);
        assignmentResponse.setTaskId(1L);
        assignmentResponse.setAssigneeId("USER_123");
        assignmentResponse.setAssignedBy("USER_456");
        assignmentResponse.setStrategy("MANUAL");
        assignmentResponse.setAssignedAt(LocalDateTime.now());

        ApiResponseDto<AssignmentResponse> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED,
                "Success",
                assignmentResponse
        );

        when(assignmentService.createAssignment(any(AssignmentDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(apiResponse));

        // Act & Assert
        mockMvc.perform(post("/api/v1/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.taskId").value(1))
                .andExpect(jsonPath("$.data.assigneeId").value("USER_123"));

        verify(assignmentService, times(1)).createAssignment(any(AssignmentDTO.class));
    }

    @Test
    void createAssignment_shouldReturn404_whenTaskNotFound() throws Exception {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(999L);
        assignmentDTO.setAssigneeId("USER_123");

        when(assignmentService.createAssignment(any(AssignmentDTO.class)))
                .thenThrow(new RecordNotFoundException("Task with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Task with ID 999 not found"));
    }

    @Test
    void createAssignment_shouldReturn400_whenAssigneeIdIsEmpty() throws Exception {
        // Arrange
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTaskId(1L);
        assignmentDTO.setAssigneeId("   ");

        when(assignmentService.createAssignment(any(AssignmentDTO.class)))
                .thenThrow(new BadRequestException("Assignee ID is required"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("Assignee ID is required"));
    }

    @Test
    void getAssignmentsByTaskId_shouldReturnAssignmentsList() throws Exception {
        // Arrange
        Long taskId = 1L;
        AssignmentResponse assignment1 = new AssignmentResponse();
        assignment1.setId(1L);
        assignment1.setTaskId(taskId);
        assignment1.setAssigneeId("USER_123");

        AssignmentResponse assignment2 = new AssignmentResponse();
        assignment2.setId(2L);
        assignment2.setTaskId(taskId);
        assignment2.setAssigneeId("USER_456");

        ApiResponseDto<List<AssignmentResponse>> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                Arrays.asList(assignment1, assignment2)
        );

        when(assignmentService.getAssignmentsByTaskId(taskId))
                .thenReturn(ResponseEntity.ok(apiResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/assignments/task/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data[0].assigneeId").value("USER_123"))
                .andExpect(jsonPath("$.data[1].assigneeId").value("USER_456"));

        verify(assignmentService, times(1)).getAssignmentsByTaskId(taskId);
    }

    @Test
    void getAssignmentsByTaskId_shouldReturn404_whenTaskNotFound() throws Exception {
        // Arrange
        Long taskId = 999L;
        when(assignmentService.getAssignmentsByTaskId(taskId))
                .thenThrow(new RecordNotFoundException("Task with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/assignments/task/{taskId}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Task with ID 999 not found"));
    }

    @Test
    void deleteAssignment_shouldReturnNoContent() throws Exception {
        // Arrange
        Long assignmentId = 1L;
        when(assignmentService.deleteAssignment(assignmentId))
                .thenReturn(ResponseEntity.noContent().build());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/assignments/{id}", assignmentId))
                .andExpect(status().isNoContent());

        verify(assignmentService, times(1)).deleteAssignment(assignmentId);
    }

    @Test
    void deleteAssignment_shouldReturn404_whenAssignmentNotFound() throws Exception {
        // Arrange
        Long assignmentId = 999L;
        when(assignmentService.deleteAssignment(assignmentId))
                .thenThrow(new RecordNotFoundException("Assignment with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/assignments/{id}", assignmentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Assignment with ID 999 not found"));
    }
}
