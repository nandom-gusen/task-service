package com.flowforge.resource.task;

import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.enums.Priority;
import com.flowforge.enums.TaskStatus;
import com.flowforge.model.Task;
import com.flowforge.repository.task.TaskRepository;
import com.flowforge.service.task.TaskService;
import com.flowforge.service.task.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskResourceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldCreateAndReturnTask() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus("TODO");
        taskDTO.setPriority("HIGH");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(Priority.HIGH);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        ResponseEntity<ApiResponseDto<TaskResponse>> response = taskService.createTask(taskDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals(HttpStatus.CREATED, response.getBody().getStatus());

        TaskResponse taskResponse = response.getBody().getData();
        assertNotNull(taskResponse);
        assertEquals("Test Task", taskResponse.getTitle());
        assertEquals("Test Description", taskResponse.getDescription());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_shouldReturnPaginatedTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setStatus(TaskStatus.TODO);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreatedAt(LocalDateTime.now());

        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2));
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        // Act
        ResponseEntity<ApiResponseDto<Page<TaskResponse>>> response =
                taskService.getAllTasks(0, 20, "createdAt", Sort.Direction.DESC);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        Page<TaskResponse> responsePage = response.getBody().getData();
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        assertEquals("Task 1", responsePage.getContent().get(0).getTitle());
        assertEquals("Task 2", responsePage.getContent().get(1).getTitle());

        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getTaskById_shouldReturnTaskWhenExists() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        ResponseEntity<ApiResponseDto<TaskResponse>> response = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        TaskResponse taskResponse = response.getBody().getData();
        assertNotNull(taskResponse);
        assertEquals(taskId, taskResponse.getId());
        assertEquals("Test Task", taskResponse.getTitle());
        assertEquals("Test Description", taskResponse.getDescription());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void deleteTask_shouldDeleteTaskWhenExists() {
        // Arrange
        Long taskId = 1L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDTO updateDTO = new UpdateTaskDTO();
        updateDTO.setId(taskId);
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus("IN_PROGRESS");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        ResponseEntity<ApiResponseDto<TaskResponse>> response = taskService.updateTask(taskId, updateDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        TaskResponse taskResponse = response.getBody().getData();
        assertNotNull(taskResponse);
        assertEquals("Updated Title", taskResponse.getTitle());
        assertEquals("Updated Description", taskResponse.getDescription());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

}
