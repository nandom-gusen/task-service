package com.flowforge.service.task;

import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.exceptions.BadRequestException;
import com.flowforge.model.Task;
import com.flowforge.repository.task.TaskRepository;
import com.flowforge.utils.EntityMapperUtil;
import com.flowforge.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.flowforge.utils.AppMessages.*;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> createTask(TaskDTO taskDTO) {

        Task task = EntityMapperUtil.mapTaskDTOtoTask(taskDTO);
        Task savedTask = taskRepository.save(task);
        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(savedTask);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<TaskResponse>>> getAllTasks(int page, int size, String sortBy, Sort.Direction sortDir) {

        Utils.validateSortField(sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        Page<Task> taskPage = taskRepository.findAll(pageable);

        Page<TaskResponse> responsePage = taskPage.map(EntityMapperUtil::mapTaskToTaskResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        responsePage)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> getTaskById(Long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            return null;
        }

        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(taskOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> updateTask(Long id, UpdateTaskDTO taskDTO) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            return null;
        }

        if(taskDTO.getId() == null){
            return null;
        }

        Task task = EntityMapperUtil.mapUpdateTaskDTOtoTask(taskDTO);
        Task savedTask = taskRepository.save(task);

        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(savedTask);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            return null;
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
