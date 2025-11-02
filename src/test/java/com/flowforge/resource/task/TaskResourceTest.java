package com.flowforge.resource.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowforge.config.SpringDataWebConfig;
import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.enums.TaskStatus;
import com.flowforge.security.jwt.JwtUtils;
import com.flowforge.service.task.TaskService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(SpringDataWebConfig.class)
@WebMvcTest(controllers = TaskResource.class,
        excludeAutoConfiguration = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class TaskResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private com.flowforge.security.jwt.AuthTokenFilter authTokenFilter;


    @MockitoBean
    private TaskService taskService;

    @BeforeEach
    void forwardFilterChain() throws Exception {
        // make the mocked filter call the filter chain so requests reach the controller
        doAnswer(invocation -> {
            ServletRequest req = invocation.getArgument(0);
            ServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res); // important: forward the request
            return null;
        }).when(authTokenFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Task Description");
        taskDTO.setStatus("TODO");
        taskDTO.setPriority("HIGH");

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("New Task");
        taskResponse.setDescription("Task Description");
        taskResponse.setStatus(TaskStatus.TODO.label);
        taskResponse.setCreatedAt(LocalDateTime.now());

        ApiResponseDto<TaskResponse> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED,
                "Success",
                taskResponse
        );

        when(taskService.createTask(any(TaskDTO.class)))
                .thenReturn(org.springframework.http.ResponseEntity.status(HttpStatus.CREATED).body(apiResponse));

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("New Task"))
                .andExpect(jsonPath("$.data.description").value("Task Description"))
                .andExpect(jsonPath("$.data.status").value("TODO"));

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    void getAllTasks_shouldReturnPaginatedTasks() throws Exception {
        // Arrange
        TaskResponse task1 = new TaskResponse();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setStatus(TaskStatus.TODO.label);

        TaskResponse task2 = new TaskResponse();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.IN_PROGRESS.label);

        Page<TaskResponse> taskPage = new PageImpl<>(Arrays.asList(task1, task2));

        ApiResponseDto<Page<TaskResponse>> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                taskPage
        );

        when(taskService.getAllTasks(anyInt(), anyInt(), anyString(), any(Sort.Direction.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(apiResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.content[0].title").value("Task 1"))
                .andExpect(jsonPath("$.data.content[1].title").value("Task 2"));

        verify(taskService, times(1)).getAllTasks(0, 20, "createdAt", Sort.Direction.DESC);
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Test Task");
        taskResponse.setDescription("Test Description");
        taskResponse.setStatus(TaskStatus.TODO.label);

        ApiResponseDto<TaskResponse> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                taskResponse
        );

        when(taskService.getTaskById(taskId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(apiResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.id").value(taskId))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.data.description").value("Test Description"));

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        UpdateTaskDTO updateDTO = new UpdateTaskDTO();
        updateDTO.setId(taskId);
        updateDTO.setTitle("Updated Task");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus("IN_PROGRESS");

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Updated Task");
        taskResponse.setDescription("Updated Description");
        taskResponse.setStatus(TaskStatus.IN_PROGRESS.label);

        ApiResponseDto<TaskResponse> apiResponse = new ApiResponseDto<>(
                true,
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                taskResponse
        );

        when(taskService.updateTask(eq(taskId), any(UpdateTaskDTO.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(apiResponse));

        // Act & Assert
        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.title").value("Updated Task"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));

        verify(taskService, times(1)).updateTask(eq(taskId), any(UpdateTaskDTO.class));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        // Arrange
        Long taskId = 1L;
        when(taskService.deleteTask(taskId))
                .thenReturn(org.springframework.http.ResponseEntity.noContent().build());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTask(taskId);
    }

}
