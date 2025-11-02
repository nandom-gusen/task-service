package com.flowforge.service.task;

import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    ResponseEntity<ApiResponseDto<TaskResponse>>  createTask(TaskDTO taskDTO);
    ResponseEntity<ApiResponseDto<Page<TaskResponse>>> getAllTasks(int page, int size, String sortBy, Sort.Direction sortDir);
    ResponseEntity<ApiResponseDto<TaskResponse>> getTaskById(Long id);
    ResponseEntity<ApiResponseDto<TaskResponse>> updateTask(Long id, UpdateTaskDTO taskDTO);
    ResponseEntity<Void> deleteTask(Long id);
}
